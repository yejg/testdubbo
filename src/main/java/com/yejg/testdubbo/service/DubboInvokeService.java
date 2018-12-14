package com.yejg.testdubbo.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.yejg.testdubbo.model.PackageInfo;
import com.yejg.testdubbo.util.JsonUtil;
import com.yejg.testdubbo.util.PackageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DubboInvokeService {
    private static final Logger logger = LoggerFactory.getLogger(DubboInvokeService.class);

    @Value("${dubbo.zookeeper}")
    private String address;

    @Value("${dubbo.interface}")
    private String basePackagePath;

    private Lock lock = new ReentrantLock();
    private List<PackageInfo> packageInfoList = new ArrayList<PackageInfo>();
    private Map<String, GenericService> serviceMap = new ConcurrentHashMap<String, GenericService>();

    @PostConstruct
    public void initPackageInfo() throws Exception {
        checkConfig();
        this.packageInfoList = getPackageInfo(basePackagePath);
    }

    private void checkConfig() throws Exception {
        if (StringUtils.isBlank(basePackagePath)) {
            throw new Exception("【dubbo.interface】未配置，请配置后重启");
        }
        if (StringUtils.isBlank(address)) {
            logger.warn("【dubbo.zookeeper】未配置，将使用默认值[127.0.0.1:2181]");
            this.address = "127.0.0.1:2181";
        }
    }

    public Object invokeDubbo(String myinterface, String mymethod, List<String> params) throws Exception {
        PackageInfo packageInfo = null;
        for (PackageInfo one : packageInfoList) {
            if (one.getMyInterface().equals(myinterface)) {
                packageInfo = one;
                break;
            }
        }
        if (packageInfo == null) {
            throw new Exception("调用的接口不存在");
        }
        String[] invokeParamTyps = packageInfo.getMyMethods().get(mymethod);
        if (invokeParamTyps == null) {
            throw new Exception("调用方法不存在");
        }
        Object[] invodeParams = new Object[invokeParamTyps.length];
        if (params.size() != invokeParamTyps.length) {
            throw new Exception("参数个数错误");
        }
        for (int i = 0; i < invokeParamTyps.length; i++) {
            //此处需要做类型转换后调用
			/*if(invokeParamTyps[i].indexOf(".model")>-1){
				Map<String,String> oneParam = JsonUtil.toObject(params.get(i),Map.class);
				invodeParams[i] = oneParam;
			}else{*/
            invodeParams[i] = params.get(i);
//			}
        }

        GenericService genericService = this.createGenericService(myinterface);
        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke(mymethod, invokeParamTyps, invodeParams);
        if (result != null) {
            logger.info(ToStringBuilder.reflectionToString(result, ToStringStyle.MULTI_LINE_STYLE));
        } else {
            result = "该方法无返回结果";
        }
		/*Map<String,Object> map = new HashMap<String, Object>();
		map.put("branch_no", "100");
		Object result = genericService.$invoke(mymethod, new String[] {"com.yejg.xpe.context.usercenter.model.Custinfo"}, new Object[] {map});
		System.err.println(ToStringBuilder.reflectionToString(result, ToStringStyle.MULTI_LINE_STYLE));*/
        return result;
    }

    /**
     * 传入全类名获得对应类中所有方法名和参数名
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, String[]> getMethodInfo(String pkgName) {
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        try {
            Class clazz = Class.forName(pkgName);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                String[] params = new String[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> clas = parameterTypes[i];
                    params[i] = clas.getName();
                }
                result.put(methodName, params);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取目录下的所有接口信息，组装返回成PackageInfo对象返回
     * @param packagepaths
     * @return
     * @throws Exception
     */
    public List<PackageInfo> getPackageInfo(String packagepaths) throws Exception {
        List<PackageInfo> list = new ArrayList<PackageInfo>();
        String[] packagepathArray = packagepaths.split(",");
        for (String packagepath : packagepathArray) {
            List<String> interfaces = PackageUtil.getClassName(packagepath, false);
            for (String name : interfaces) {
                PackageInfo packaeInfo = new PackageInfo();
                packaeInfo.setMyInterface(name);
                packaeInfo.setMyMethods(getMethodInfo(name));
                list.add(packaeInfo);
            }
        }
        logger.info("PackageInfoList:{}", JsonUtil.toJsonStr(list));
        return list;
    }

    public List<PackageInfo> getPackageInfoList() {
        logger.info("packageInfoList的size=",packageInfoList.size());
        return packageInfoList;
    }

    private GenericService createGenericService(String myinterface) {
        GenericService genericService = null;
        if (this.serviceMap.containsKey(myinterface)) {
            genericService = this.serviceMap.get(myinterface);
        }
        // 当前应用配置
        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用

        lock.lock();
        try {
            if (this.serviceMap.containsKey(myinterface)) {
                return this.serviceMap.get(myinterface);
            }
            ApplicationConfig application = new ApplicationConfig();
            application.setName("testdubbo");

            // 连接注册中心配置
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol("zookeeper");
            registry.setAddress(address);
            // 引用远程服务
            // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setApplication(application);
            reference.setRegistry(registry);
            // 弱类型接口名
            reference.setInterface(myinterface);
            reference.setValidation("");
            // 声明为泛化接口
            reference.setGeneric(true);
            genericService = reference.get();
            this.serviceMap.put(myinterface, genericService);
        } finally {
            lock.unlock();
        }
        return genericService;
    }

    public static void main(String[] args) throws Exception {
		/*packageInfoList= getPackageInfo("com.yejg.xpe.dubbo.user.api");
		List<String> params = new  ArrayList<String>();
		params.add("100222");
		invokeDubbo("com.yejg.xpe.dubbo.user.api.ICustinfoMagService","qryCustinfoById",params);*/
    }
}
