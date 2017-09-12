var sPop = null;
var postSubmited = false;

document.write("<style type='text/css'id='defaultPopStyle'>");
document.write(".cPopText { font-family: Tahoma, Verdana; background-color: #FFFFCC; border: 1px #000000 solid; font-size: 12px; padding-right: 4px; padding-left: 4px; line-height: 18px; padding-top: 2px; padding-bottom: 2px; visibility: hidden; filter: Alpha(Opacity=80)}");

document.write("</style>");
document.write("<div id='popLayer' style='position:absolute;z-index:1000' class='cPopText'></div>");

function showPopupText(event) {
	if(event.srcElement) o = event.srcElement; else o = event.target;
	MouseX=event.clientX;
	MouseY=event.clientY;
	if(o.alt!=null && o.alt!="") { o.pop=o.alt;o.alt="" }
	if(o.title!=null && o.title!=""){ o.pop=o.title;o.title="" }
	if(o.pop!=sPop) {
		sPop=o.pop;
		if(sPop==null || sPop=="") {
			document.getElementById("popLayer").style.visibility="hidden";        
		} else {
			if(o.dyclass!=null) popStyle=o.dyclass; else popStyle="cPopText";
			document.getElementById("popLayer").style.visibility="visible";
			showIt();
		}
	}
}

function showIt() {
	document.getElementById("popLayer").className=popStyle;
	document.getElementById("popLayer").innerHTML=sPop.replace(/<(.*)>/g,"&lt;$1&gt;").replace(/\n/g,"<br>");;
	popWidth=document.getElementById("popLayer").clientWidth;
	popHeight=document.getElementById("popLayer").clientHeight;
	if(MouseX+12+popWidth>document.body.clientWidth) popLeftAdjust=-popWidth-24; else popLeftAdjust=0;
	if(MouseY+12+popHeight>document.body.clientHeight) popTopAdjust=-popHeight-24; else popTopAdjust=0;
	document.getElementById("popLayer").style.left=MouseX+12+document.body.scrollLeft+popLeftAdjust;
	document.getElementById("popLayer").style.top=MouseY+12+document.body.scrollTop+popTopAdjust;
}

function ctlent(event) {
	if(postSubmited == false && (event.ctrlKey && event.keyCode == 13) || (event.altKey && event.keyCode == 83)) {
		if(this.document.input.pmsubmit) {
			postSubmited = true;
			this.document.input.pmsubmit.disabled = true;
			this.document.input.submit();
		} else if(validate(this.document.input)) {
			postSubmited = true;
			if(this.document.input.topicsubmit) this.document.input.topicsubmit.disabled = true;
			if(this.document.input.replysubmit) this.document.input.replysubmit.disabled = true;
			if(this.document.input.editsubmit) this.document.input.editsubmit.disabled = true;
			this.document.input.submit();
		}
	}
}

function checkall(form, prefix, checkall) {
	var checkall = checkall ? checkall : 'chkall';
	for(var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if(e.name != checkall && (!prefix || (prefix && e.name.match(prefix)))) {
			e.checked = form.elements(checkall).checked;;
		}
	}
}

function findobj(n, d) {
	var p, i, x;
	if(!d) d = document;
	if((p = n.indexOf("?"))>0 && parent.frames.length) {
		d = parent.frames[n.substring(p + 1)].document;
		n = n.substring(0, p);
	}
	if(x != d[n] && d.all) x = d.all[n];
	for(i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
	for(i = 0; !x && d.layers && i < d.layers.length; i++) x = findobj(n, d.layers[i].document);
	if(!x && document.getElementById) x = document.getElementById(n);
	return x;
}


function copycode(obj) {
	var rng = document.body.createTextRange();
	rng.moveToElementText(obj);
	rng.scrollIntoView();
	rng.select();
	rng.execCommand("Copy");
	rng.collapse(false);
}

function toggle_collapse(objname) {
	obj = findobj(objname);
	img = findobj(objname+"_img");
	collapsed = getcookie("discuz_collapse");
	cookie_start = collapsed ? collapsed.indexOf(objname) : -1;
	cookie_end = cookie_start + objname.length + 1;

	if(obj.style.display == "none") {
		obj.style.display = "";
		img_re = new RegExp("_yes\\.gif$");
		img.src = img.src.replace(img_re, '_no.gif');
		if(cookie_start != -1) collapsed = collapsed.substring(0, cookie_start) + collapsed.substring(cookie_end, collapsed.length);
	} else {
		obj.style.display = "none";
		img_re = new RegExp("_no\\.gif$");
		img.src = img.src.replace(img_re, '_yes.gif');
		if(cookie_start == -1) collapsed = collapsed + objname + " ";
	}

	expires = new Date();
	expires.setTime(expires.getTime() + (collapsed ? 86400 * 30 : -(86400 * 30 * 1000)));
	document.cookie = "discuz_collapse=" + escape(collapsed) + "; expires=" + expires.toGMTString() + "; path=/";
}

function imgzoom(o) {
	if(event.ctrlKey) {
		var zoom = parseInt(o.style.zoom, 10) || 100;
		zoom -= event.wheelDelta / 12;
		if(zoom > 0) {
			o.style.zoom = zoom + '%';
		}
		return false;
	} else {
		return true;
	}
}

function getcookie(name) {
	var cookie_start = document.cookie.indexOf(name);
	var cookie_end = document.cookie.indexOf(";", cookie_start);
	return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));
}

/*if(!document.onmouseover) {
	try{
		document.onmouseover = function(e) {
			if (!e) showPopupText(window.event); else showPopupText(e);
		};
	}catch(e) {}
}*/

/**
 * 全选/反选 zileng 2010.10.25
 */
function selectAll(id){
	var checkboxs=document.getElementsByTagName("input");
    var checkbox = document.getElementById(id);
    var i; 
    for(i=0;i<checkboxs.length;i++) { 
       if(checkboxs[i].type=='checkbox'){ 
          if(checkbox.checked == true){
             checkboxs[i].checked = true;
          }
          if(checkbox.checked == false){
             checkboxs[i].checked = false;
          }
       } 
    } 
}

function trim(s){
	return s.replace(/(^\s+)|(\s+$)/g,'');
}

function pass(id){
	if(checkSelect()){
		if( document.forms['form2'].elements[id].value.length > 200 ){
			alert('备注不能超过200个！');
			return false;
		}
		if( confirm('确认审核通过？') ){
			return true;
		}else{
			return false;
		}
	}
	alert("请至少选择一条记录！");
	return false;
}


function reject(id){
	if(checkSelect()){
		if( trim(document.forms['form2'].elements[id].value) == '' ){
			alert('请在备注中输入退回原因！');
			return false;
		}
		if( document.forms['form2'].elements[id].value.length > 200 ){
			alert('备注不能超过200个！');
			return false;
		}
		if( confirm('确认退回解冻申请？') ){
			return true;
		}else{
			return false;
		}
	}
	alert("请至少选择一条记录！");
	return false;
}

function checkPass(){
	if(checkSelect()){
		document.getElementById('passDiv').style.display = "";
		return true;
	}
	alert("请至少选择一条记录！");
	return false;
	
}

function checkReject(){
	if(checkSelect()){
		document.getElementById('rejectDiv').style.display = "";
		return true;
	}
	alert("请至少选择一条记录！");
	return false;
	
}


function cancelDiv(id){
	document.getElementById(id).style.display = "none";
}

function checkSelect(){
	if(document.form2.ids == null){
		return false;
	}
	var count=document.form2.ids.length;
	if(count == null && document.form2.ids!=null && document.form2.ids.checked){
		return true;
	}
	var j=0;
	if(count!=null){
		for(var i=0;i<count;i++){
			if (document.form2.ids[i].checked){
				++j;
			}
		}
	}
	
	if(j==0){
		return false;
	}else{
		return true;
	}
}

