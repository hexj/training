 document.write("<div id='divSingleChoiceTree' style='position: absolute; z-index: 99999999; overflow:auto;width: 300; height: 300; display: none'>");
document.write("<table border=\"1\" cellspacing=\"0\">");
document.write("<tr>");
document.write("<td>");

document.write("<iframe name='iframeSingleChoiceTree' scrolling='yes' frameborder='0' width='100%' height='240'></iframe>");

document.write("</td>");
document.write("</tr>");
document.write("<tr>");
document.write("<td>");
document.write("<table id=\"tableSingleChoiceTree\" width=\"100%\" style=\"background:#E5EEF5\">");
document.write("<tr>");
document.write("<td id=\"tdClear\" align=\"center\"><input id=\"inputClear\" type=\"button\" value=\"清除\" onclick=\"onClear()\"/></td>");
document.write("<td id=\"tdOK\" align=\"center\"><input id=\"inputOK\" type=\"button\" value=\"确定\" onclick=\"onOK()\"/></td>");
document.write("<td id=\"tdClose\" align=\"center\"><input id=\"inputClose\" type=\"button\" value=\"关闭\" onclick=\"onClose()\"/></td>");
document.write("</tr>");
document.write("</table>");
document.write("</td>");
document.write("</tr>");
document.write("</table>");
document.write("</div>");

var selectedElement=null;
var inputTitle=null;
var inputValue=null;

function getAbsLeft(obj)
{
	if(obj.offsetParent==null)
	{
		return obj.offsetLeft;
	}
	else
	{
		var leftParent=getAbsLeft(obj.offsetParent);
		return obj.offsetLeft+leftParent;
	}
}

function getAbsTop(obj)
{
	if(obj.offsetParent==null)
	{
		return obj.offsetTop;
	}
	else
	{
		var topParent=getAbsTop(obj.offsetParent);
		return obj.offsetTop+topParent;
	}
}

var jsonTreeNode;

function showChoiceTree(inputVN,inputT,bForLeafNode)
{	
	if(bForLeafNode==null || bForLeafNode=="")
	{
		bForLeafNode=false;
	}
		
	var sUrl = "/mckinley/crm/knowledge/createBizTypeToJason.htm?rn="+Math.floor(Math.random()*999+1);
	//创建一个请求
	if(jsonTreeNode=="" || jsonTreeNode == "undefined" || jsonTreeNode == null ){
		//alert("jsonTreeNode == undefined");		
		//YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
		muAsyncRequest(sUrl);
	}
	innerTreeNode(jsonTreeNode,inputT,inputVN,bForLeafNode)	
	document.getElementById("divSingleChoiceTree").style.display="";
	//alert("finish");
}

function initJason(){
//请求成功后的处理	
	var sUrl = "/mckinley/crm/knowledge/createBizTypeToJason.htm?rn="+Math.floor(Math.random()*999+1);
	//创建一个请求
	if(jsonTreeNode=="" || jsonTreeNode == "undefined" || jsonTreeNode == null ){		
		//YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
		muAsyncRequest(sUrl);
	}
}

function innerTreeNode(jsonTreeNode,inputT,inputVN,bForLeafNode){
	
	var e = inputT;

	selectedElement=null;
	inputTitle=null;
	inputValue=null;
	inputValue=document.getElementById(inputVN);

	inputTitle=e;
	

	var nDivX=getAbsLeft(e);
	var nDivY=getAbsTop(e)+e.offsetHeight;

//	var nDivY=0;
//	var nAbsTop=getAbsTop(e);
//	nDivY=nAbsTop+e.offsetHeight;
//	if(nAbsTop>=300 && nDivY+300>document.body.offsetHeight)
//	{
//		nDivY=nAbsTop-300+e.offsetHeight;
//	}
	var strHTML="";	
	strHTML+="<html>";
	strHTML+="<head><meta http-equiv='Content-Type' content='text/html; charset=GBK'></head>";
	strHTML+="<body onkeydown='return false' onselectstart='return false' style='margin: 0px' oncontextmenu='return false'>";
	strHTML+=getTreeHTML(jsonTreeNode,1,0,inputValue.value,bForLeafNode);	
	strHTML+="</body>";
	strHTML+="</html>";
	iframeSingleChoiceTree.document.writeln(strHTML);
	iframeSingleChoiceTree.document.close();
	

	if(inputValue.value!="")
	{
		with(iframeSingleChoiceTree)
		{
			var tds=document.childNodes(0).childNodes(1).childNodes(0).all;
			for(var i=0;i<tds.length;i++)
			{
				if(tds(i).tagName.toLowerCase()!="td")
				{
					continue;
				}
				if(tds(i).value==null)
				{
					continue;
				}
				if(tds(i).value==inputValue.value)
				{
					selectedElement=tds(i).previousSibling.childNodes(0);
					break;
				}
				
			}
			if(selectedElement!=null)
			{//依次打开父节点
				var trNode=selectedElement.parentNode.parentNode;
				while(trNode.parentNode.parentNode.parentNode.tagName.toLowerCase()!="body")
				{
					trNode.parentNode.parentNode.parentNode.parentNode.style.childflag=2;
					trNode.parentNode.parentNode.parentNode.parentNode.style.display="block";
					trNode.parentNode.parentNode.parentNode.parentNode.previousSibling.children(0).children(0).src="/mckinley/static/images/treeIcon/OpenCross.png";
					
					
					trNode=trNode.parentNode.parentNode.parentNode.parentNode;
				}
			}
			
			
		}
	}
	var divTree = document.getElementById("divSingleChoiceTree");
	divTree.style.posLeft=nDivX;
	divTree.style.posTop=nDivY;
	divTree.style.display="block";
	iframeSingleChoiceTree.focus();
	
	
}

function getTreeHTML(treeNode,nSiblingCount,nIndex,strInputValue,bForLeafNode)
{
	//alert("//getTreeHTML:"+bForLeafNode);
	var strHTML="";
	strHTML+="<table style=\"font-size:9pt;padding:0;\" cellpadding=\"0\" cellspacing=\"0\">";

	strHTML+=getTreeNodeHTML(treeNode,nSiblingCount,nIndex,strInputValue,bForLeafNode);

	strHTML+="</table>";
	
	return strHTML;
}

function getTreeNodeHTML(treeNode,nSiblingCount,nIndex,strInputValue,bForLeafNode)
{
	//alert("//getTreeNodeHTML:"+bForLeafNode);
	var strHTML="";
	
	if(treeNode.constructor!=window.Array)
	{//非数组
		//alert("非数组");
		if(treeNode.children==null || treeNode.children.length==0)
		{//叶子节点
			strHTML+="<tr>";
			
			if(nIndex+1<nSiblingCount)
			{
				strHTML+="<td><img src=\"/mckinley/static/images/treeIcon/TConnector.png\" onclick=\"parent.onOpenClose()\" childflag=\"0\"></td>";
			}
			else
			{
				strHTML+="<td><img src=\"/mckinley/static/images/treeIcon/LConnector.png\" onclick=\"parent.onOpenClose()\" childflag=\"0\"></td>";
			}
			
			if(strInputValue!=treeNode.id)
			{
				strHTML+="<td style=\"cursor:hand\"><img src=\"/mckinley/static/images/treeIcon/check0.gif\" onclick=\"parent.onSelect()\" selectflag=\"1\"></td>";
				strHTML+="<td style=\"cursor:hand\" value=\""+treeNode.id+"\" path=\""+treeNode.path+"\"  onclick=\"parent.onSelect()\"><nobr>"+treeNode.name+"</nobr></td>";
			}
			else
			{
				strHTML+="<td style=\"cursor:hand\"><img src=\"/mckinley/static/images/treeIcon/check2.gif\" onclick=\"parent.onSelect()\" selectflag=\"2\"></td>";
				strHTML+="<td style=\"cursor:hand;color:#FF0000\" value=\""+treeNode.id+"\"  path=\""+treeNode.path+"\" onclick=\"parent.onSelect()\"><nobr>"+treeNode.name+"</nobr></td>";
			}
			
			strHTML+="</tr>";
			
			return strHTML;
		}
		else
		{//非叶子节点
			strHTML+="<tr>";
	
			if(nIndex+1<nSiblingCount)
			{
				strHTML+="<td><img src=\"/mckinley/static/images/treeIcon/CloseCross.png\" onclick=\"parent.onOpenClose()\" childflag=\"1\" lastchildflag=\"0\"></td>";
			}
			else
			{
				strHTML+="<td><img src=\"/mckinley/static/images/treeIcon/CloseCross.png\" onclick=\"parent.onOpenClose()\" childflag=\"1\" lastchildflag=\"1\"></td>";
			}
			
			if(bForLeafNode==false)
			{//所有节点都能选
			//alert("//所有节点都能选:"+bForLeafNode);
				if(strInputValue!=treeNode.id)
				{
					strHTML+="<td style=\"cursor:hand\"><img src=\"/mckinley/static/images/treeIcon/check0.gif\" onclick=\"parent.onSelect()\" selectflag=\"1\"></td>";
					strHTML+="<td style=\"cursor:hand\" value=\""+treeNode.id+"\" path=\""+treeNode.path+"\"  onclick=\"parent.onSelect()\"><nobr>"+treeNode.name+"</nobr></td>";
				}
				else
				{
					strHTML+="<td style=\"cursor:hand\"><img src=\"/mckinley/static/images/treeIcon/check2.gif\" onclick=\"parent.onSelect()\" selectflag=\"2\"></td>";
					strHTML+="<td style=\"cursor:hand;color:#FF0000\" value=\""+treeNode.id+"\" path=\""+treeNode.path+"\" onclick=\"parent.onSelect()\"><nobr>"+treeNode.name+"</nobr></td>";
				}
			}
			else
			{//只能选择叶子节点
			//alert("//只能选择叶子节点:"+bForLeafNode);
				strHTML+="<td><img src=\"/mckinley/static/images/treeIcon/Connector.png\"></td>";
				strHTML+="<td><nobr>"+treeNode.name+"</nobr></td>";
			}
			
			strHTML+="</tr>";

			
			strHTML+="<tr style='display:none'>";
			if(nIndex+1<nSiblingCount)
			{
				strHTML+="<td style=\"background-image:url('/mckinley/static/images/treeIcon/IConnector.png')\"></td>";
			}
			else
			{
				strHTML+="<td></td>";
			}
	
			strHTML+="<td></td>";
			strHTML+="<td>";
			
			for(var i=0;i<treeNode.children.length;i++)
			{
				var node=treeNode.children[i];
				
				strHTML+=getTreeHTML(treeNode.children[i],treeNode.children.length,i,strInputValue,bForLeafNode);
			}
			strHTML+="</td>";
			strHTML+="</tr>";
		}
	}
	else
	{
		//alert("数组");
		for(var i=0;i<treeNode.length;i++)
		{
			strHTML+=getTreeNodeHTML(treeNode[i],treeNode.length,i,strInputValue,bForLeafNode);
		}
	}
	
		
	
	return strHTML;
}

function onOpenClose()
{
	var e = iframeSingleChoiceTree.event.srcElement;
	if(e.childflag=="0")
	{
	}
	else if(e.childflag=="1")
	{
		e.parentNode.parentNode.nextSibling.style.display="block";
		if(e.lastchildflag==0)
		{
			e.src="/mckinley/static/images/treeIcon/OpenCross.png";
		}
		else
		{
			e.src="/mckinley/static/images/treeIcon/LOpenCross.png";
		}
		e.childflag="2";
	}
	else
	{
		e.parentNode.parentNode.nextSibling.style.display="none";
		if(e.lastchildflag==0)
		{
			e.src="/mckinley/static/images/treeIcon/CloseCross.png";
		}
		else
		{
			e.src="/mckinley/static/images/treeIcon/LCloseCross.png";
		}
		e.childflag="1";
	}
}

function onSelect()
{
	var e = iframeSingleChoiceTree.event.srcElement;
	
	if(e.tagName.toLowerCase()=="nobr")
	{
		e=e.parentNode.previousSibling.childNodes(0);
	}
	else if(e.tagName.toLowerCase()=="td")
	{
		e=e.previousSibling.childNodes(0);
	}
	
	if(e.selectflag=="0")
	{
	}
	if(e.selectflag=="1")
	{
		if(e!=null)
		{
			e.src="/mckinley/static/images/treeIcon/check0.gif";
			e.selectflag="1";
			e.parentNode.nextSibling.style.color="#000000";
			
			inputTitle.value=e.parentNode.nextSibling.path;//显示全路径
			inputValue.value=e.parentNode.nextSibling.value;
		}
		e.src="/mckinley/static/images/treeIcon/check2.gif";
		e.selectflag="2";
		e.parentNode.nextSibling.style.color="#FF0000";
		selectedElement=e;
	}
	
	divSingleChoiceTree.style.display="none";
	
}



function onClear()
{
	if(selectedElement==null)
	{
		divSingleChoiceTree.style.display="none";
		return;
	}
	
	selectedElement.src="/mckinley/static/images/treeIcon/check0.gif";
	selectedElement.selectflag="1";
	selectedElement.parentNode.nextSibling.style.color="#000000";
	
	selectedElement=null;
	inputTitle.value="";
	inputValue.value="";
	divSingleChoiceTree.style.display="none";
}

function onOK()
{
	if(selectedElement==null)
	{
		inputTitle.value="";
		inputValue.value="";
	}
	else
	{		
		//inputTitle.value=selectedElement.parentNode.nextSibling.childNodes(0).innerText;//显示title
		inputTitle.value=selectedElement.parentNode.nextSibling.path;//显示全路径
		inputValue.value=selectedElement.parentNode.nextSibling.value;

		selectedElement.src="/mckinley/static/images/treeIcon/check0.gif";
		selectedElement.selectflag="1";
		selectedElement.parentNode.nextSibling.style.color="#000000";
	}
	
	document.getElementById("divSingleChoiceTree").style.display="none";
}

function onClose()
{
	document.getElementById("divSingleChoiceTree").style.display="none";
}


function queryTitle(treeNode,value)
{
	if(treeNode.constructor!=window.Array)
	{
		if(treeNode.id==value)
		{
			return treeNode.name;
		}

		var nLength=0;
		if(treeNode.children!=null)
		{
			nLength=treeNode.children.length;
		}
		if(nLength==0)
		{
			return null;
		}
		for(var i=0;i<nLength;i++)
		{
			var valueTemp=queryTitle(treeNode.children[i],value);
			if(valueTemp!=null)
			{
				return valueTemp;
			}
		}
	}
	else
	{
		var nLength=treeNode.length;

		for(var i=0;i<nLength;i++)
		{
			var valueTemp=queryTitle(treeNode[i],value);
			if(valueTemp!=null)
			{
				return valueTemp;
			}
		}
	}

	return null;
}


function initxmlhttp()
{
	  var xmlhttp
	  try {
	    xmlhttp=new ActiveXObject("Msxml2.XMLHTTP");
	  } catch (e) {
	     try {
	        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	     } catch (E) {
	        xmlhttp=false;
	     }
	  }
	if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
	        try {
	                xmlhttp = new XMLHttpRequest();
	        } catch (e) {
	                xmlhttp=false;
	        }
	}
	if (!xmlhttp && window.createRequest) {
	        try {
	                xmlhttp = window.createRequest();
	        } catch (e) {
	                xmlhttp=false;
	        }
	}
	  return xmlhttp;
}

function muAsyncRequest(url)
{ 
  var xmlhttp=initxmlhttp();
  xmlhttp.open("GET",url,false);
  xmlhttp.setRequestHeader("Cache-Control","no-cache");
  xmlhttp.onreadystatechange=function(){
     if(xmlhttp.readyState==4 && xmlhttp.status==200){
            if(xmlhttp.responseText!="null")
			{	
				var json = eval("(" + xmlhttp.responseText + ")");
				jsonTreeNode = json ;				
		    }
     }
  }
  xmlhttp.send(null);
}



document.onclick = function() 
{
    if(window.event.srcElement!=inputTitle && window.event.srcElement!=inputClear && window.event.srcElement!=inputOK && window.event.srcElement!=inputClear && window.event.srcElement!=tdClear && window.event.srcElement!=tdOK && window.event.srcElement!=tdClose && window.event.srcElement!=tableSingleChoiceTree)
    {
		document.getElementById("divSingleChoiceTree").style.display="none";
    }
}

window.onload = function(){	
	//initJason();
}
