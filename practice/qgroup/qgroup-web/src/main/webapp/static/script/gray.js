function editapp(appid){
	$.getJSON("/gray/yyAction.do?appid="+appid,
      {
		action:"YyAction",
		event_submit_do_get_app_info_json:"1"
      },
      function(data) {
        $("#appdiv-app-id").val(data.appId);
        $("#appdiv-app-name").val(data.appName);
        $("#appdiv-daily-ip-a").val(data.appDailyIpA);
        $("#appdiv-daily-ip-b").val(data.appDailyIpB);
        $("#appdiv-prepub-ip-a").val(data.appPrepubIpA);
        $("#appdiv-prepub-ip-b").val(data.appPrepubIpB);
        $("#appdiv-online-ip-a").val(data.appOnlineIpA);
        $("#appdiv-online-ip-b").val(data.appOnlineIpB);
        
        $("#app-daily-a-port").val(data.appDailyAPort);
        $("#app-daily-b-port").val(data.appDailyBPort);
        $("#app-prepub-a-port").val(data.appPrepubAPort);
        $("#app-prepub-b-port").val(data.appPrepubBPort);
        $("#app-online-a-port").val(data.appOnlineAPort);
        $("#app-online-b-port").val(data.appOnlineBPort);
        
        $("#appdiv-cookie-domain-daily").val(data.appCookieDomainDaily);
        $("#appdiv-cookie-domain-online").val(data.appCookieDomainOnline);
        
        $("#app-ck-w-domain-daily").val(data.appCkWDomainDaily);
        $("#app-ck-w-domain-online").val(data.appCkWDomainOnline);
        
        $("#appdiv-cookie-index").val(data.appCookieIndex);
        $("#updateappdiv").toggle();
      });
}

function editProcess(pid){
	//doGetProcessJson
	$.getJSON("/gray/fbdAction.do?pid="+pid,
      {
		action:"FbdAction",
		event_submit_do_get_process_json:"1"
      },
      function(data) {
      	$("#udpatediv-pid").val(data.pId);
        $("#udpatediv-pname").val(data.pName);
        $("#udpatediv-pusername").val(data.pUserName);
        $("#udpatediv-pmembership").val(data.pMembership);
        $("#updatediv-st").val(1+data.pStartTime.month+"/"+data.pStartTime.date+"/"+(1900+data.pStartTime.year));
        $("#updatediv-et").val(1+data.pEndTime.month+"/"+data.pEndTime.date+"/"+(1900+data.pEndTime.year));
        $("#updatediv input:checkbox").val(data.pAppids.split(","));
        $("#updatediv input:radio").val((data.pStatus+"").split(""));
        $("#updatediv").toggle();
      });
}
	
function getCookieValue(appid){
	if(appid==""){
        $("#cookieA").val("");
        $("#cookieB").val("");
		return;
	}
	$.getJSON("/gray/cookieAction.do?appId="+appid,
      {
		action:"CookieAction",
		event_submit_do_get_generate_cookie_value:"1"
      },
      function(data) {
        $("#cookieA").val(data.A);
        $("#cookieB").val(data.B);
      });
}

function putTempNicks(rid,oneip,nickids){
	var count = $("#putnick_process_"+rid).html();
	$("#putnick_process_"+rid).html(count+" <img alt='������' title='������' src='http://www.galaxytheatres.com/css/img/ajax-loader.gif'/>");

  $.getJSON("/gray/ruleAction.do?rid="+rid+"&oneip="+oneip+"&ns="+nickids,
  {
	action:"RuleAction",
	event_submit_do_puttempnick:"1"
  },
  function(data) {
  	if(data.success+""=="false"){
  		alert("����"+data.errmsg);
  	}else{
  		alert(data.succmsg);
  	}
  	showTempNicks();
  	$("#putnick_process_"+rid).html(count);
  });
}

function putNicks(rid,oneip){
	var count = $("#putnick_process_"+rid).html();
	$("#putnick_button_"+rid).hide();
	$("#putnick_process_"+rid).html(count+" <img alt='������' title='������' src='http://www.galaxytheatres.com/css/img/ajax-loader.gif'/>");

  $.getJSON("/gray/ruleAction.do?rid="+rid+"&oneip="+oneip,
  {
	action:"RuleAction",
	event_submit_do_putnick:"1"
  },
  function(data) {
  	if(data.success+""=="false"){
  		alert("����"+data.errmsg);
  	}else{
  		alert(data.succmsg);
  		count=parseInt(count)+1;
  	}
  	$("#putnick_button_"+rid).show();
  	$("#putnick_process_"+rid).html(count);
  });
}

function queryIpsStatus(rid){
	  //console.time('queryIpsStatus');
	  $("#rule_status_process").html(" <img alt='������' title='������' src='http://www.galaxytheatres.com/css/img/ajax-loader.gif'/>");
	  $.getJSON("/gray/ruleAction.do?rid="+rid,
	  {
		action:"RuleAction",
		event_submit_do_query_ips:"1"
	  },
	  function(data) {
	  	if(data.success+""=="false"){
	  		//alert("����"+data.errmsg);
	  	}else{
	  		for(var k in data.module){
	  			$('#s_'+k.replace(/\./g,'_')).html(data.module[k]);
	  		}
	  		//console.log(new Date().toLocaleTimeString());
	  		//alert("ͬ�����");
	  	}
	  	
	  	$('input[type=checkbox]').each(function () {
	  		this.checked = false;
	  	});
	  	$("#rule_status_process").html("");
	  	reloadbutton();
	  	//console.timeEnd('queryIpsStatus');
	  });
}


function doClearRule(rid){
  var ips = allcheckbox();
  if(ips=="blank"){
  	return;
  }
  allButton(true);
  
  if((current_status=="0"||current_status=="2")&&ips==1){
  	var count = 0;
  	$('input[type=checkbox][name=cb]').each(function () {
    			count++;
    });
    if(count!=1){
	  	alert("�ӷ������������������һ�β���ȫ���������밴�����������");
	  	allButton(false);
	  	return;
    }
  }
  
  $.getJSON("/gray/ruleAction.do?rid="+rid+"&ips="+ips,
  {
	action:"RuleAction",
	event_submit_do_clear:"1"
  },
	function(data) {
	  	if(data.success+""=="false"){
	  		alert("����"+data.errmsg);
	  		allButton(false);
	  	}else{
	  		alert("������ɡ�");
	  		allButton(false);
	  		location.reload();
	  	}
	});
}

function doInitRule(rid){
  var ips = allcheckbox();
  if(ips=="blank"){
  	return;
  }
  allButton(true);
  
  $.getJSON("/gray/ruleAction.do?rid="+rid+"&ips="+ips,
	  {
		action:"RuleAction",
		event_submit_do_init:"1",
	  },
	function(data) {
	  	if(data.success+""=="false"){
	  		if(data.errMsg=="cookie_error"){
				uLightBox.alert({
	                width: '700px',
	                title: 'cookie����',
	                rightButtons: ['���û���COOKIE', '�����þ�COOKIE'],
	                leftButtons: ['Close'],
	                opened: function() {
	                    $('<span />').html("�ò��Ե�Cookies���� ǰ���ñ�ʹ�ù���Ϊ�˷�ֹ������Ŀ���û������뵱ǰ�����غϣ�ǿ�ҽ�������֮��<br>���ȷ��ԭ��ʹ�õ�cookie���ؾ��ǵ�ǰ��Ŀ��ʹ�õģ����Լ���ʹ�á�").appendTo('#lbContent');
	                },
	                onClick: function(button) {
	                    if (button == '���û���COOKIE') {
							  $.getJSON("/gray/cookieAction.do?rid="+rid+"&pid="+ips,
							  {
								action:"CookieAction",
								event_submit_do_resetcookies:"1"
							  },
								function(data) {
								  	if(data.success+""=="false"){
								  		alert("����"+data.errMsg);
								  		allButton(false);
								  	}else{
								  		alert("���óɹ���");
								  		allButton(false);
								  		location.reload();
								  	}
								  });
	                    }
	                    if (button == '�����þ�COOKIE') {
							  $.getJSON("/gray/cookieAction.do?rid="+rid+"&pid="+ips,
							  {
								action:"CookieAction",
								event_submit_do_use_old_cookies:"1"
							  },
							  function(data) {
							  	if(data.success+""=="false"){
							  		alert("����"+data.errMsg);
							  		allButton(false);
							  	}else{
							  		uLightBox.destroy();
							  		allButton(false);
							  	}
							  });
	                    }
	                    //console.log(button);
	                }
                });
	  		}else{
		  		alert("����"+data.errMsg);
		  		allButton(false);
	  		}
	  	}else{
	  		alert("��ʼ����ɡ�");
	  		allButton(false);
	  		location.reload();
	  	}
	  });
}

function doStartRule(rid){
  if(!checkBeta()){
  	return;
  }
  //console.time('doStartRule');
  var ips = allcheckbox();
  if(ips=="blank"){
  	return;
  }
  allButton(true);
  
  if(current_status=="1"&&ips==1){
  	var count = 0;
  	$('input[type=checkbox][name=cb]').each(function () {
    			count++;
    });
    if(count!=1){
	  	alert("���ز�������һ�β���ȫ������������ѡ�񲿷ֻ������ء�");
	  	allButton(false);
	  	return;
    }
  }
 
  $.getJSON("/gray/ruleAction.do?rid="+rid+"&ips="+ips,
  {
	action:"RuleAction",
	event_submit_do_start:"1"
  },
	function(data) {
	  	if(data.success+""=="false"){
	  		alert("����"+data.errMsg);
	  		allButton(false);
	  	}else{
		  	//console.timeEnd('doStartRule');
	  		alert("�����ɹ���");
	  		allButton(false);
	  		location.reload();
	  	}
	  });
}

function doStopRule(rid){
  if(!checkBeta()){
  	return;
  }
  //console.time('doStopRule');
  var ips = allcheckbox();
  if(ips=="blank"){
  	return;
  }
  allButton(true);
  
  if(current_status=="1"&&ips==1){
  	var count = 0;
  	$('input[type=checkbox][name=cb]').each(function () {
    			count++;
    });
    if(count!=1){
	  	alert("���ز�������һ�β���ȫ������������ѡ�񲿷ֻ������ء�");
    	allButton(false);
	  	return;
    }
  }
  $.getJSON("/gray/ruleAction.do?rid="+rid+"&ips="+ips,
  {
	action:"RuleAction",
	event_submit_do_stop:"1"
  },
	function(data) {
	  	if(data.success+""=="false"){
	  		alert("����"+data.errMsg);
	  		allButton(false);
	  	}else{
		  	//console.timeEnd('doStopRule');
	  		alert("��ͣ�ɹ���");
	  		allButton(false);
	  		location.reload();
	  	}
	  });
}

function doTestMuliRule(){
	if($.trim($('#rName').val())==""){
		alert("����������Ϊ�ա�");
		return false;
	}
	if($.trim($('#app').val())==""){
		alert("Ӧ�ò���Ϊ�ա�");
		return false;
	}
	if($.trim($('#cookieB').val())==""){
		alert("cookie���ز���Ϊ��");
		return false;
	}
	if($.trim($('#cookieA').val())==""){
		alert("cookie���ز���Ϊ��");
		return false;
	}
	$('#loadbutton').attr("disabled",true);
	$('#methood').attr("name","event_submit_do_test_muli_rule");

	$.post("/gray/ruleMuliAction.do?event_submit_do_test_muli_rule=1&_input_charset=utf-8",
    $("#ruleform").serialize(),
	function(data){
	  	//console.timeEnd('doTestRule');
	  	if(data.success+""=="false"){
	  		alert("����"+data.errMsg);
	  	}else{
			alert("Ч��ɹ�");
	  		$('#addsubmit').attr('disabled',false);
	  	}
	  	$('#loadbutton').attr("disabled",false);
	  },"json");
	$('#methood').attr("name","event_submit_do_muli_add");
}


function doTestRule(){
  //console.time('doTestRule');
  if($('#muliRule_1').attr("checked")){
  	doTestMuliRule();
  	return;
  }
  if(!addrulecheck()){
  	return;
  }
  $('#loadbutton').attr("disabled",true);
  $('#methood').attr("name","event_submit_do_test_rule");
  $.post("/gray/ruleAction.do?event_submit_do_test_rule=1&_input_charset=utf-8",
    $("#ruleform").serialize(),
	function(data){
	  	//console.timeEnd('doTestRule');
	  	if(data.success+""=="false"){
	  		alert("����"+data.errMsg);
	  	}else{
	  		alert("Ч��ɹ�");
	  		$('#addsubmit').attr('disabled',false);
	  	}
	  	$('#loadbutton').attr("disabled",false);
	  },"json");
   $('#methood').attr("name","event_submit_do_add");
}

function addrulecheck(){
	if($.trim($('#rName').val())==""){
		alert("����������Ϊ�ա�");
		return false;
	}
	if($.trim($('#app').val())==""){
		alert("Ӧ�ò���Ϊ�ա�");
		return false;
	}
	if($.trim($('#cookieB').val())==""){
		alert("cookie���ز���Ϊ��");
		return false;
	}
	if($.trim($('#cookieA').val())==""){
		alert("cookie���ز���Ϊ��");
		return false;
	}
	if(!$('#rSplitFlowType1').attr("checked")&&!$('#rSplitFlowType2').attr("checked")&&!$('#rSplitFlowType3').attr("checked")){
		alert("����ѡ��һ�ַ�����ʽ");
		return false;
	}
	if($.trim($('#rAccessIps').val()).length>0){
		var patrn=/^[0-9]|[a-z]|[.-]|[,]{1,500}$/;
		if(!patrn.exec($.trim($('#rAccessIps').val()))){
			alert("IP����ֻ����500λ���ڵ� ָ��ʡ��Сдȫƴ������ . - , ��ɣ������л��У�����ո�ʽ�޸ġ�");
			return false;
		}
		if($.trim($('#rAccessIps').val()).indexOf("\n")>=0||$.trim($('#rAccessIps').val()).indexOf("\r")>=0){
			alert("IP����ֻ����500λ���ڵ�  ָ��ʡ��Сдȫƴ������ . - , ��ɣ������л��У�����ո�ʽ�޸ġ�");
			return false;
	    }
    }
	return true;
}


function querybetaappstatus(rid){
  allButton(true);
  $.getJSON("/gray/ruleAction.do?rid="+rid,
  {
	action:"RuleAction",
	event_submit_do_query_beta_status:"1"
  },
	function(data) {
	  	for(var k in data.module){
	  		$('#b_'+k.replace(/\./g,'_')).html(data.module[k]);
	  	}
	  	allButton(false);
	});
}

function loadIp(formName){
	var appName = $.trim($("#appdiv-app-name").val());
	if(appName==''){
		alert('Ӧ��������Ϊ��');
		return;
	}
	var site = "";
	if(formName=='appform'){
		$(name=appform).find("input[name=site]:checked").each(function(){
			site = site+(","+$(this).val());
		});
	}else if(formName == 'updateform'){
		$(name=updateform).find("input[name=site]:checked").each(function(){
			site = site+(","+$(this).val());
		});
	}
	$.get("appIp.do", {appName:appName,site:site},function(data){
		if(data.success=='true'){
			if(formName=='appform'){
				$(name=appform).find("#appdiv-online-ip-a").val(data.ips);
			}else if(formName == 'updateform'){
				$(name=updateform).find("#appdiv-online-ip-a").val(data.ips);
			}
			alert("��ȡ�ɹ�!");
		}else{
			alert(data.errMsg);
		}
    },"json");
}
	



