/*封装了常用的控件，如分页工具条、类目选择框等
 * @author:shihong 
 * @date:2007-10-15
 */
String.prototype.Trim = function() 
{ 
    return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 
String.prototype.LTrim = function() 
{ 
return this.replace(/(^\s*)/g, ""); 
} 

String.prototype.RTrim = function() 
{ 
return this.replace(/(\s*$)/g, ""); 
} 


/**
 * 常用页面工具条
 * 
 * 使用方法：
 * PageTools.attach(elementIds,config,baseUrl,paramters)
 * elementIds： id数组，格式 {parent：父标签,totalItem:总共条数标签,totalPage:总共页数标签,currentPage:当前页码标签,lines:每页条数标签}
 * config： 业务逻辑对象，包含了相关的业务逻辑判断，如赋结果值，逻辑校验
 * baseUrl：基本请求url，为字符串类型
 * paramters：附加参数，为数组类型
 */
var PageTools= new function (){
	//var Y = YAHOO.util;
	var jumpText="jumpto";
	var elementIds;
	var paramters;
	var baseUrl;
	var config={};
	
	var getUrl= function (newpage){
		var url="";
		var i=0;
		
		url=baseUrl+"?currentPage="+newpage;
		for(var obj in paramters){
			var val=paramters[obj];
			var value="";
		    if(obj.indexOf('_')>=0){//paramter in parent frame 
		        var objName=obj.substring(obj.indexOf('_')+1,obj.length);
		        if(document.getElementById(objName))
		        	value=parent.document.getElementById(objName).value;
		    }else{
		    	if(document.getElementById(obj))
		        	value=document.getElementById(obj).value;
		    }
			
			url=url+"&"+val+"="+value;
			i++;
		}
		url=url+ "&t=" + new Date().getTime();
		return url;
	}
	
	var gotoPage=function gotoPage(newpage) {
		if(config.check){
			if(!config.check())
			    return;
		}
		var url=getUrl(newpage);

		//alert("url1="+url);

		window.location.href=url;
		
	}

	
	var createInput=function(name,typeName){
		var input = document.createElement('input');
		input.type="button";
		input.value=name;
		input.onclick=function(type,currentPage,totalPage){
	        return function (){ 
			     var newpage=0;
				 var tolPage=parseInt(document.getElementById(totalPage).value,10);
			     var curPage=parseInt(document.getElementById(currentPage).value,10);
				 
		       	switch (type) {
			       case 'firstPage':
				        newpage=1;
						break;
			       case 'prePage':
				        newpage=curPage-1;
						break;
			       case 'nextPage':
				        newpage=curPage+1;
						break;
			       case 'lastPage':
			            newpage = tolPage;
			            break;
			       default:
			      	   break;
			   }
			   if(newpage!=0)
			       gotoPage(newpage);
		   };
		}(typeName,elementIds.currentPage, elementIds.totalPage);
		return input;
	}
	
	var keyPressFunc = function(event){	
				try {
					var c = 0;
					
					if (document) {
						//c = window.event.keyCode; 
						//edited by buchen at 2010-08-12 17:38
						//because it does not support firefox
						var event = arguments[0]||window.event; 
						c = event.keyCode;
					}
					if (c == 13) {
				    	var newPage = document.getElementById('jumpto').value;
                        var totalPage = document.getElementById(elementIds.totalPage).value;
				    	if(parseInt(newPage,10)>totalPage)
                              newPage=totalPage;
						gotoPage(newPage);
						return false;
					}
				}catch (e) {
			     	alert(e);
				}
				return true;
			}
			
	var createGotoText=function (){
		var input = document.createElement('input');
		input.type="text";
		input.size="3";
		input.id=input.name=jumpText;
		
		if (input.addEventListener) { 
		//Safari,opera,firefox 
			input.addEventListener("keypress", keyPressFunc, false); 
		} 
		else if (input.attachEvent) { 
		//IE
			input.attachEvent("onkeypress", keyPressFunc); 
		} 
		else { 
			input.onkeypress = keyPressFunc; 
		} 
	
		return input;
	}
	
	var validateArguments = function(pelementIds,pconfig,pbaseUrl,pparamters){
		var parentElementId=pelementIds.parent;
		var parent;
		//父元素必须存在
		if(document.getElementById(parentElementId)){
			parent=document.getElementById(parentElementId)
		}else{
			alert('没有此元素!');
			return false;
		}
		
		//父元素必须是table
		if(parent.tagName.toLowerCase()!="table"){
			alert('需要table作为父标签!');
			return false;
		}
		
		//初始化
		 elementIds=pelementIds;
	     paramters=pparamters;
	     baseUrl=pbaseUrl;
	     config=pconfig||{};
		return true;
	}
	
	var initPageTools=function (){
		//alert("in:"+elementIds.parent);
		var parentElementId=document.getElementById(elementIds.parent);
	    var tr=parentElementId.insertRow(parentElementId.rows.length);
		var totalItem=document.getElementById(elementIds.totalItem).value;
		var totalPage=document.getElementById(elementIds.totalPage).value;
		var currentPage=document.getElementById(elementIds.currentPage).value;
		
		//初始化第一个TD，初始化提示信息
		var td1=tr.insertCell(tr.cells.length);
		td1.innerHTML="共 "+totalItem+" 条 "+totalPage+" 页 *当前第 "+currentPage+" 页*";
		
		
		//初始化第二个TD，初始化页面跳转按钮
		var td2=tr.insertCell(tr.cells.length);
		if(parseInt(currentPage,10)>1){
			td2.appendChild(createInput('首页','firstPage'));
			td2.appendChild(createInput('上一页','prePage'));
		}
		if(parseInt(currentPage,10)<parseInt(totalPage,10)){
			td2.appendChild(createInput('下一页','nextPage'));
			td2.appendChild(createInput('末页','lastPage'));
		}
		
		//初始化跳转框
		if(parseInt(totalPage,10)>1){
			var td3=tr.insertCell(tr.cells.length);
			td3.innerHTML+="转到";
			td3.appendChild(createGotoText());	
		}
	}
	
	
	this.attach= function (elementIds,config,baseUrl,paramters){
		var handle={};
		if(validateArguments(elementIds,config,baseUrl,paramters)){//验证
			initPageTools();//初始化页面跳转的工具条
		}
		
		return handle;
	}
}();

/**
 * 类目属性选择框
 * 
 * 使用方法：
 * 
 */
var MoveAuctionSelector=new function(){
    var Y = YAHOO.util;
    var url="getSubCatOrProp.htm";//异步请求的url /mckinley/auction/commodity/
	
	
     //构建异步请求
	var getUrl=function(con){
	   var fullUrl=url+"?id="+con.id+"&type="+con.type+"&isLeaf="+con.isLeaf+ "&t=" + new Date().getTime();
	   return fullUrl;
	};
	
	//截取找到的第一个字符串
	var getSubString=function(str,parent){
	    var result="";
		if(typeof str == "string"){
			var index=parent.indexOf(str); 
			result=parent.substring(0,index);
		}
		return result;
	}
	
     var unDisabledCkbox=function(sel,seleType){
         return function(){
                if(sel.value==""){
                   if(seleType=="addDel"){//set delete checkbox disabled
                      var inputs=TB.common.toArray(sel.parentNode.getElementsByTagName("input"));
                      for(var i=0;i<inputs.length;i++){
                         if(inputs[i].type=="checkbox"){
                             inputs[i].checked=false;
                             inputs[i].disabled=true; 
                         }
                      } 
                   }
                     return;
                }
                  //set propert 
                  
               if(seleType=="addDel"&&sel.value.indexOf(":")>=0){
                  var inputs=TB.common.toArray(sel.parentNode.getElementsByTagName("input"));
                  for(var i=0;i<inputs.length;i++){
                       if(inputs[i].type=="checkbox"){
                           inputs[i].disabled=false;
                       }
                  }
               }
         
        }   
 };
	//构建选择框onchange处理函数
	var getChangeFunc=function(sel,seleType){
	    return function(){
            //clear children
            removeAllChildren(sel);

			if(sel.value==""){
                if(seleType=="addDel"){//set delete checkbox disabled
                      var inputs=TB.common.toArray(sel.parentNode.getElementsByTagName("input"));
                      for(var i=0;i<inputs.length;i++){
                         if(inputs[i].type=="checkbox"){
                             inputs[i].checked=false;
                             inputs[i].disabled=true;
                         }
                      } 
                }
			    return;
            }
       
            //set propert delete choice
            if(seleType=="addDel"&&sel.value.indexOf(":")>=0){
                  var inputs=TB.common.toArray(sel.parentNode.getElementsByTagName("input"));
                  for(var i=0;i<inputs.length;i++){
                       if(inputs[i].type=="checkbox"){
                           inputs[i].disabled=false;
                       }
                  }
            }
            //getSubCatOrProperty
			var id=sel.value;
			var isleaf = sel.options[sel.selectedIndex].text.indexOf(">>")>=0?0:1;
			var type = sel.value.indexOf(":")>=0?"property":"category";
			var condition={id:id,type:type,isLeaf:isleaf};
			var url=getUrl(condition);
			
			Y.Connect.asyncRequest('GET', url, {
				success: function(req) {
					//var ss= getSubString('<div',req.responseText);
				    //var json = eval( ss );
        
					var json = eval( req.responseText );
					removeAllChildren(sel);//clear for anthor time
					buildSelector(json,sel,seleType);
				},
				failure: function(req) {
					alert("系统错误:" + req.status);
			    }
		    });
        }			
	};
	
	//根据属性JSON结点生成属性选择框
	var genSelectorByJson=function(jsonNode,type){
	    var select  = document.createElement('select');
        select.options[select.options.length] = new Option('', '');
        //moveid,default value	
		if(jsonNode.type=="property"){//如果jsonNode为属性结点
             var ischild=false;
             for(var j = 0; j < jsonNode.val.length; j++){
				var opt = jsonNode.val[j];
				if(jsonNode.val[j].ischild=="1"){
                    ischild=true;
					select.onchange=getChangeFunc(select,type);
				}
				select.options[select.options.length] = new Option(opt.name, opt.id);
			}
            if(!ischild){//no child
                    select.onchange=unDisabledCkbox(select,type);
            }
		}else{//如果jsonNode为类目结点
            select.onchange=getChangeFunc(select,type);	
			for (var i = 0; i < jsonNode.length; i++) {
				var opt = jsonNode[i];
				select.options[select.options.length] = new Option(opt.name, opt.id);
			}
          
		}	
		return select;
	};
	
	//添加类目的属性选择框
	var addchildrenOfCat=function(json,sel,type){
		var ltable=sel.parentNode.parentNode.parentNode;
		var ptr;
	    for(var i = 0; i < json.length; i++){
	            ptr=ltable.insertRow(ltable.rows.length);
				var ntd1=ptr.insertCell(ptr.cells.length);
				addDelCheckBox(ntd1,type,json[i].id);
				ntd1.innerHTML =ntd1.innerHTML+json[i].name+"：";
				//var ntd2=ptr.insertCell(ptr.cells.length);
				var select = genSelectorByJson(json[i],type);
				ntd1.appendChild(select);
		}
	};
	
	//添加属性的子属性选择框
	var addchildrenOfProp=function(json,sel,type){
	    var ptr=sel.parentNode.parentNode;
		for(var i = 0; i < json.length; i++){
			var ntd1=ptr.insertCell(ptr.cells.length);
			addDelCheckBox(ntd1,type,json[i].id);
			ntd1.innerHTML =ntd1.innerHTML +json[i].name+"：";
			//var ntd2=ptr.insertCell(ptr.cells.length);
			var select  = genSelectorByJson(json[i],type);
			ntd1.appendChild(select);
		}
	};
	
	var addDelCheckBox = function ( tdObj,type,id){
	    if(type=="addDel"){
			var ckbox  = document.createElement('input');
			ckbox.value=id;
			ckbox.type="checkbox";
            ckbox.disabled=true;
			tdObj.innerHTML="删除";
			tdObj.appendChild(ckbox);
		}
	}
	
	//根据返回的结果集构建选择框
	var buildSelector=function(json,sel,type){
		if(json[0]&&json[0].type=="category"){
			var ptr=sel.parentNode.parentNode;
			var ntd=ptr.insertCell(ptr.cells.length);
			var select  = genSelectorByJson(json,type);
			ntd.appendChild(select);
		}else if(json[0]&&json[0].type=="property"){
			if(sel.value.indexOf(":")>=0)//如果父结点是属性选择框
			    addchildrenOfProp(json,sel,type);
			else if(type=="property"||type=="addDel"){         //如果父结点是类目选择框
			    addchildrenOfCat(json,sel,type);
				}
		}
	};
	
	var initSelector=function(json,name,type){
		removeAllChildren(document.getElementById(name));	
	    var select=genSelectorByJson(json,type);
		var sourcetr=document.getElementById(name).insertRow(0);
		var sourcetd=sourcetr.insertCell(0);
		sourcetd.appendChild(select);
	};
	
	//清空子级选择框
	var removeAllChildren=function(obj){
	    if(obj.tagName.toLowerCase()=="table"){
		   for(var i=obj.rows.length-1;i>=0;i--){
		      obj.deleteRow(i);
		   }
		}else{
		    var realParent=obj.parentNode.parentNode
			var rIndex=realParent.rowIndex;
            var index=obj.parentNode.cellIndex;
			
			//清掉本行
            for(var i=realParent.cells.length-1;i>index;i--){
			      realParent.deleteCell(i);
		    }
			
			//清掉其它行
		    if(rIndex==0){
			     var ltable=obj.parentNode.parentNode.parentNode;
				 for(var i=ltable.rows.length-1;i>0;i--){
				     ltable.deleteRow(i);
				 }
			}
		}			
	};
	
	this.attach = function(elementID,type){
		var handle = {};// 类似可公用的接口
		//初始化
		
		var condition={id:0,type:"category",isLeaf:0};		
		var url=getUrl(condition);
		Y.Connect.asyncRequest('GET', url, {
				success: function(req) {
					//var ss= getSubString('<div',req.responseText);
                    //var json = eval( ss );
					//alert("text:"+ss.substring(4050));
					var json = eval( req.responseText );
					initSelector(json,elementID,type);
				},
				failure: function(req) {
					alert("系统错误:" + req.status);
			    }
		});
	
		
		//return handle;
	}
 };