import request from '../utils/request';
import qs from 'qs';
import {notification,message } from 'antd'
export default {

  namespace: 'indexpage',

  state: {
      interfaces:[]
  },

  subscriptions: {
   setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/') {
          dispatch({ type: 'fetch',
            payload: {} })
        }
      })
    },
  },

  effects: {
    *fetch({ payload }, { call, put }) {  // eslint-disable-line
     const data = yield call(query, payload);
      yield put({ type: 'save' ,
                  payload: { data: data}
                });
    },
    *invoke({ payload }, { call, put }){
      const result = yield call(invokeReq,payload);
      yield invokeResult(result.data);
    }
  },

  reducers: {
    save(state, action) {
      return { ...state, "interfaces":action.payload.data.data };
    }
  },

};

async function query(payload) {
  return request('/allinterface');
}

async function invokeReq(payload){
  return request('/invokeservice',{
    method: 'POST',
    headers: {
    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
    },
    body: qs.stringify(payload)
  });
}

function invokeResult(result){
  console.log(result);
   if(result.error_no && "-1"==result.error_no){
        message.error(result.error_info);
   }else{
     notification.open({
        message: '调用结果',
        description: JSON.stringify(result.result)
      })
   }
}
