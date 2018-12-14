import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'dva';
import styles from './invoke.less';
import { Form, Icon,Input, Button, Checkbox,Row, Col,Select} from 'antd';
const FormItem = Form.Item;
const Option = Select.Option;
const ButtonGroup = Button.Group;
const { TextArea } = Input;

class Invoke extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      interfaces:[],
      currinterface:{}
    };
  }
  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.props.handleSubmit(values);
      }
    });
  }
  interfacesChange = (value) =>{
     const interfaces  = this.state.interfaces;
      for (var i = 0; i < interfaces.length; i++) {
          if(interfaces[i].myInterface == value){
              this.setState({currinterface:interfaces[i]});
              this.props.form.setFieldsValue({
                mymethod: '',
              });
              return;
          }
      }
  }
  clearForm = () =>{
    this.props.form.resetFields();
  }
  
  clearParam=()=>{
    this.props.form.setFieldsValue({
        params: '',
      });
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    this.state.interfaces = this.props.param.interfaces;
    const interfaces  = this.props.param.interfaces;
    var options=[];
    if(interfaces){
        options  =  interfaces.map((item,index) => {
         return (<Option key={index} value={item.myInterface}>{item.myInterface}</Option>);
      })
    }
    const currinterface=this.state.currinterface;
    const methodoptions=[];
    if(currinterface){
      for(var p in currinterface.myMethods){
         methodoptions.push((<Option key={p} value={p}>{p}</Option>));
      }
    }
    const formItemLayout = {
      labelCol:{span: 3 },
      wrapperCol:{ span: 21},
    };
    return (
       <Row  style={{height:"100%"}}>
          <Col span={6} style={{height:"100%"}}>
              <Form onSubmit={this.handleSubmit} className="login-form">
                <FormItem
                  {...formItemLayout}
                  label="接口"
                 >
                  {getFieldDecorator('myinterface', {
                    rules: [{ required: true, message: '请选择接口！' }]
                  })(
                    <Select showSearch  
                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                     onChange ={this.interfacesChange} placeholder="请选择接口"
                     >
                     {options}
                    </Select>
                  )}
                </FormItem>
                <FormItem
                  {...formItemLayout}
                  label="方法"
                 >
                  {getFieldDecorator('mymethod', {
                    rules: [{ required: true, message: '请选择方法！' }]
                  })(
                    <Select showSearch 
                    filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                    placeholder="请选择方法"
                    >
                     {methodoptions}
                    </Select>
                  )}
                </FormItem>
                 <FormItem
                  {...formItemLayout}
                  label="参数"
                 >
                  {getFieldDecorator('params', {
                    rules: []
                  })(
                    <TextArea rows={10} />
                  )}
                </FormItem>
                
                <FormItem>
                 <ButtonGroup>
                   <Button type="primary" htmlType="submit" className="login-form-button">
                    调用
                  </Button>
                  <Button type="primary" className="login-form-button" onClick={this.clearForm}>
                    重置
                  </Button>
                   <Button type="primary" className="login-form-button" onClick={this.clearParam}>
                    清理参数
                  </Button>
                </ButtonGroup>
                 
                 
                </FormItem>
              </Form>
          </Col>
         <Col span={18} style={{height:"100%"}}>
            <div style={{height: "100%",width: "100%"}}>
              <iframe id="jsoncn" src="https://www.json.cn" style={{height: "100%",width: "100%",border:"solid 1px #E5EBEE",marginLeft:2}} scrolling="no">
              </iframe>
          </div>
         </Col>
      </Row>
    );
  }
}

export default Form.create()(Invoke);
