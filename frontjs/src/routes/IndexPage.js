import React from 'react';
import { connect } from 'dva';
import styles from './IndexPage.css';
import Invoke from '../components/invoke';

function IndexPage({location, dispatch, indexpage}) {
  function realInvoke (values){
	dispatch({
		 type: 'indexpage/invoke',
		 payload:values
	});
  }
  return (
    <div className={styles.normal}>
      <Invoke param={indexpage} handleSubmit={realInvoke}  />
    </div>
  );
}

IndexPage.propTypes = {
};
export default connect(state => ({
  indexpage: state.indexpage
}))(IndexPage);
