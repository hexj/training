/*��װ�˳��õĿؼ������ҳ����������Ŀѡ����
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
 * ����ҳ�湤����
 * 
 * ʹ�÷�����
 * PageTools.attach(elementIds,config,baseUrl,paramters)
 * elementIds�� id���飬��ʽ {parent������ǩ,totalItem:�ܹ�������ǩ,totalPage:�ܹ�ҳ����ǩ,currentPage:��ǰҳ���ǩ,lines:ÿҳ������ǩ}
 * config�� ҵ���߼����󣬰�������ص�ҵ���߼��жϣ��縳���ֵ���߼�У��
 * baseUrl����������url��Ϊ�ַ�������
 * paramters�����Ӳ�����Ϊ��������
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
		//��Ԫ�ر������
		if(document.getElementById(parentElementId)){
			parent=document.getElementById(parentElementId)
		}else{
			alert('û�д�Ԫ��!');
			return false;
		}
		
		//��Ԫ�ر�����table
		if(parent.tagName.toLowerCase()!="table"){
			alert('��Ҫtable��Ϊ����ǩ!');
			return false;
		}
		
		//��ʼ��
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
		
		//��ʼ����һ��TD����ʼ����ʾ��Ϣ
		var td1=tr.insertCell(tr.cells.length);
		td1.innerHTML="�� "+totalItem+" �� "+totalPage+" ҳ *��ǰ�� "+currentPage+" ҳ*";
		
		
		//��ʼ���ڶ���TD����ʼ��ҳ����ת��ť
		var td2=tr.insertCell(tr.cells.length);
		if(parseInt(currentPage,10)>1){
			td2.appendChild(createInput('��ҳ','firstPage'));
			td2.appendChild(createInput('��һҳ','prePage'));
		}
		if(parseInt(currentPage,10)<parseInt(totalPage,10)){
			td2.appendChild(createInput('��һҳ','nextPage'));
			td2.appendChild(createInput('ĩҳ','lastPage'));
		}
		
		//��ʼ����ת��
		if(parseInt(totalPage,10)>1){
			var td3=tr.insertCell(tr.cells.length);
			td3.innerHTML+="ת��";
			td3.appendChild(createGotoText());	
		}
	}
	
	
	this.attach= function (elementIds,config,baseUrl,paramters){
		var handle={};
		if(validateArguments(elementIds,config,baseUrl,paramters)){//��֤
			initPageTools();//��ʼ��ҳ����ת�Ĺ�����
		}
		
		return handle;
	}
}();

/**
 * ��Ŀ����ѡ���
 * 
 * ʹ�÷�����
 * 
 */
var MoveAuctionSelector=new function(){
    var Y = YAHOO.util;
    var url="getSubCatOrProp.htm";//�첽�����url /mckinley/auction/commodity/
	
	
     //�����첽����
	var getUrl=function(con){
	   var fullUrl=url+"?id="+con.id+"&type="+con.type+"&isLeaf="+con.isLeaf+ "&t=" + new Date().getTime();
	   return fullUrl;
	};
	
	//��ȡ�ҵ��ĵ�һ���ַ���
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
	//����ѡ���onchange������
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
					alert("ϵͳ����:" + req.status);
			    }
		    });
        }			
	};
	
	//��������JSON�����������ѡ���
	var genSelectorByJson=function(jsonNode,type){
	    var select  = document.createElement('select');
        select.options[select.options.length] = new Option('', '');
        //moveid,default value	
		if(jsonNode.type=="property"){//���jsonNodeΪ���Խ��
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
		}else{//���jsonNodeΪ��Ŀ���
            select.onchange=getChangeFunc(select,type);	
			for (var i = 0; i < jsonNode.length; i++) {
				var opt = jsonNode[i];
				select.options[select.options.length] = new Option(opt.name, opt.id);
			}
          
		}	
		return select;
	};
	
	//�����Ŀ������ѡ���
	var addchildrenOfCat=function(json,sel,type){
		var ltable=sel.parentNode.parentNode.parentNode;
		var ptr;
	    for(var i = 0; i < json.length; i++){
	            ptr=ltable.insertRow(ltable.rows.length);
				var ntd1=ptr.insertCell(ptr.cells.length);
				addDelCheckBox(ntd1,type,json[i].id);
				ntd1.innerHTML =ntd1.innerHTML+json[i].name+"��";
				//var ntd2=ptr.insertCell(ptr.cells.length);
				var select = genSelectorByJson(json[i],type);
				ntd1.appendChild(select);
		}
	};
	
	//������Ե�������ѡ���
	var addchildrenOfProp=function(json,sel,type){
	    var ptr=sel.parentNode.parentNode;
		for(var i = 0; i < json.length; i++){
			var ntd1=ptr.insertCell(ptr.cells.length);
			addDelCheckBox(ntd1,type,json[i].id);
			ntd1.innerHTML =ntd1.innerHTML +json[i].name+"��";
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
			tdObj.innerHTML="ɾ��";
			tdObj.appendChild(ckbox);
		}
	}
	
	//���ݷ��صĽ��������ѡ���
	var buildSelector=function(json,sel,type){
		if(json[0]&&json[0].type=="category"){
			var ptr=sel.parentNode.parentNode;
			var ntd=ptr.insertCell(ptr.cells.length);
			var select  = genSelectorByJson(json,type);
			ntd.appendChild(select);
		}else if(json[0]&&json[0].type=="property"){
			if(sel.value.indexOf(":")>=0)//��������������ѡ���
			    addchildrenOfProp(json,sel,type);
			else if(type=="property"||type=="addDel"){         //������������Ŀѡ���
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
	
	//����Ӽ�ѡ���
	var removeAllChildren=function(obj){
	    if(obj.tagName.toLowerCase()=="table"){
		   for(var i=obj.rows.length-1;i>=0;i--){
		      obj.deleteRow(i);
		   }
		}else{
		    var realParent=obj.parentNode.parentNode
			var rIndex=realParent.rowIndex;
            var index=obj.parentNode.cellIndex;
			
			//�������
            for(var i=realParent.cells.length-1;i>index;i--){
			      realParent.deleteCell(i);
		    }
			
			//���������
		    if(rIndex==0){
			     var ltable=obj.parentNode.parentNode.parentNode;
				 for(var i=ltable.rows.length-1;i>0;i--){
				     ltable.deleteRow(i);
				 }
			}
		}			
	};
	
	this.attach = function(elementID,type){
		var handle = {};// ���ƿɹ��õĽӿ�
		//��ʼ��
		
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
					alert("ϵͳ����:" + req.status);
			    }
		});
	
		
		//return handle;
	}
 };