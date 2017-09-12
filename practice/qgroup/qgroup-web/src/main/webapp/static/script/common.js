//wwonline
var wangwangInstalled = false;
try {
	if (document.getElementById('clientCaps').isComponentInstalled("{99560E1D-E17E-44b5-90E4-5DE25BF6F7B1}", "componentID")) {
		wangwangInstalled = true;
	}
	if (!wangwangInstalled) {
		var obj = new ActiveXObject("WangWangX.WangWangObj");
		if (obj) {
			wangwangInstalled = true;
			delete obj;
		}
	}
}catch (e) {
	//alert(e.message);
}

var exitpop=false;
function PopPage(strURL, name, width, height, left, top)
{
    if(width==null) width=800;
    if(height==null) height=500;
    if(left==null) left = ( screen.width - width ) / 2;
    if(top==null) top  = ( screen.height - height ) / 2;
    temp = "width="+width+",height="+height+",left="+left+",top="+top+",scrollbars=1,toolbar=no,location=no,directories=no,status=yes,resizable=yes";
    w = window.open(strURL,"_blank",temp);
    w.focus();
}
function openThisAbout()
{
    temp = "width=618,height=400,left=0,top=0,scrollbars=yes,toolbar=no,location=no,directories=no,status=no,resizable=no";
    window.open("/home/about/about_taobao.html","",temp);
}
function GetCookie(cookiename)
{
    var thebigcookie = document.cookie;
    var firstchar = thebigcookie.indexOf(cookiename);
    if (firstchar != -1) {
        firstchar += cookiename.length + 1;
        lastchar = thebigcookie.indexOf(";",firstchar);
        if(lastchar == -1) lastchar = thebigcookie.length;
        return unescape(thebigcookie.substring(firstchar, lastchar));
    }
    return "";
}
function setCookie(cookiename,cookievalue,cookieexpdate,domainname)
{
    document.cookie = cookiename + "=" + cookievalue
    + "; domain=" + domainname
    + "; path=" + "/"
    + "; expires=" + cookieexpdate.toGMTString();

}
function unloadpopup(cookiename,popurl,popwidth,popheight,domainname,tr)
{
    //return;
    try {
        if (!tr)
            return;
        if( GetCookie(cookiename) == "" )
        {
            var expdate = new Date();
            expdate.setTime(expdate.getTime() + 1 * (24 * 60 * 60 * 1000)); //+1 day
            setCookie(cookiename,"yes",expdate,domainname);
            if( exitpop )
            {
                w = window.open(popurl,cookiename,"width="+popwidth+",height="+popheight+",scrollbars=1,toolbar=yes,location=yes,menubar=yes,status=yes,resizable=yes");
                w.focus;
            }
        }
    }catch (e) {}
}
function setCheckboxes(theForm, elementName, isChecked)
{
    var chkboxes = document.forms[theForm].elements[elementName];
    var count = chkboxes.length;

    if (count)
    {
        for (var i = 0; i < count; i++)
        {
            chkboxes[i].checked = isChecked;
        }
    }
    else
    {
        chkboxes.checked = isChecked;
    }
    return true;
}

var imageObject;
function ResizeImage(obj, MaxW, MaxH)
{
    if (obj != null) imageObject = obj;
    var state=imageObject.readyState;
    var oldImage = new Image();
    oldImage.src = imageObject.src;
    var dW=oldImage.width; var dH=oldImage.height;
    if(dW>MaxW || dH>MaxH) {
        a=dW/MaxW; b=dH/MaxH;
        if(b>a) a=b;
        dW=dW/a; dH=dH/a;
    }
    if(dW > 0 && dH > 0)
        imageObject.width=dW;imageObject.height=dH;
    if(state!='complete' || imageObject.width>MaxW || imageObject.height>MaxH) {
        setTimeout("ResizeImage(null,"+MaxW+","+MaxH+")",40);
    }
}

//from admin/default.vm
function sel() {
	var o = document.forms[0];
	if (!o) return;
	var e = o.elements;
	for (i = 0; i < e.length; i++) {
		if (e[i].type == 'checkbox') {
			if (e[i].checked) {
				e[i].checked = false;
			}else {
				e[i].checked = true;
			}
		}
	}
}

    /*
    *@function name:查找页面对象函数
    *@param:控件名称
    */
    function findObj(controlName) { //v1.01
        var control;
        try{
            control = document.getElementsByName(controlName)[0];
            return control;
        }catch(e){
            return null;
        }
    }
    /*
    *@function name:翻页函数
    *@param:总页数、当前页数、总记录数、每页显示记录数、翻页数、FORM名称、隐藏翻页控件名称、图片连接地址
    *desc:rollpage可以不传，默认为20，增加了错误处理
    *调用方式：try{
    *   _do(100,1,100000,'','form1','dddd.d','http://img.taobao.net');
    *}catch(ex){
    *    alert('翻页输入参数传递错误');
    *}
    */
    function pageSlider(totalPage,currentPage,totalCount,perPage,rollpage,frmName,pageControl,imgUrlPrex){　//v1.01
        try{
          if ((rollpage == '') ||  isNaN(rollpage)){
            rollpage = 20;
          }
          if ((perPage == '') ||  isNaN(perPage)){
            perPage = 20;
          }
            var curPage = 0 ;
            var divisor = (currentPage - currentPage%rollpage)/rollpage;
            if ( currentPage%rollpage == 0){
                divisor = divisor - 1;
            }
            var preRollPage = ( divisor - 1 ) * rollpage + 1;
            var nextRollPage = ( divisor + 1 ) * rollpage + 1;
            var txt = "";
            txt = txt + "<table width=\"100%\" class=\"pagination\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";
            txt = txt + "<tr>";
            txt = txt + "<td align=\"left\" width=\"25%\">";
            if ( totalCount <= 0 ){
               txt = txt + 0 ;
            }else{
               txt = txt + ((currentPage - 1)*perPage + 1);
            }
            txt = txt + " - ";
            if ((currentPage*perPage) > totalCount ){
                txt = txt + totalCount;
            }else{
                txt = txt + (currentPage*perPage);
            }
            txt = txt + " 共 " + totalCount + " 条 " + totalPage + " 页";
            txt = txt + "</td>";
            txt = txt + "<td align=\"right\">";
            if (preRollPage > 0){
                txt = txt + "<a href=\"#?page=1\"><img src=\""+ imgUrlPrex + "/arrow4-1.gif\" border=\"0\" alt=\"首页\" onclick=\""+ frmName+ ".reset();"+"findObj('" + pageControl + "').value=1; " + frmName + ".submit(); \"></img></a>";
                txt = txt + "&nbsp;";
                txt = txt + "<a href=\"#?page=" + preRollPage + "\"><img src=\""+ imgUrlPrex + "/arrow3-1.gif\" border=\"0\" alt=\"前一页\" onclick=\""+ frmName+ ".reset();"+"findObj('" + pageControl + "').value="+preRollPage+"; "+ frmName + ".submit(); \"></img></a>";
            }else{
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow4-1.gif\" border=\"0\" alt=\"首页\"></img>";
                txt = txt + "&nbsp;";
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow3-1.gif\" border=\"0\" alt=\"前一页\"></img>";
            }
            txt = txt + "&nbsp;";
            for(i = 1; i <= rollpage; i++){
                curPage = rollpage*divisor + i;
                if (curPage <= totalPage){
                    if (curPage != currentPage){
                        txt = txt + "<a href=\"#?page=" +curPage+ "\" onclick=\""+ frmName+ ".reset();"+"findObj('" + pageControl + "').value=" +curPage+ "; " + frmName + ".submit(); \">" +curPage+ "</a>";
                        txt = txt + "&nbsp;";
                    }else{
                        txt = txt + "<strong>" + curPage + "</strong>";
                        txt = txt + "&nbsp;";
                    }
                }
            }
            if ( nextRollPage <= totalPage ){
                txt = txt + "<a href=\"#?page=" + nextRollPage + "\" onclick=\""+ frmName+ ".reset();"+"findObj('" + pageControl + "').value=" +nextRollPage+ "; " + frmName + ".submit(); \"><img src=\""+ imgUrlPrex + "/arrow1-1.gif\" border=\"0\" alt=\"后一页\"></img></a>";
                txt = txt + "&nbsp;";
                txt = txt + "<a href=\"#?page=" + totalPage + "\" onclick=\""+ frmName+ ".reset();"+"findObj('" + pageControl + "').value=" +totalPage+ "; " + frmName + ".submit(); \"><img src=\""+ imgUrlPrex + "/arrow2-1.gif\" border=\"0\" alt=\"尾页\"></img></a>";
            }else{
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow1-1.gif\" border=\"0\" alt=\"后一页\"></img>";
                txt = txt + "&nbsp;";
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow2-1.gif\" border=\"0\" alt=\"尾页\"></img>";
            }
            txt = txt + "</td>";
            txt = txt + "</tr>";
            txt = txt + "</table>";
            document.write(txt);
            //alert(txt);
        }catch(e){
        }
    }
    
    function pageSliderWithoutReset(totalPage,currentPage,totalCount,perPage,rollpage,frmName,pageControl,imgUrlPrex){　//v1.01
        try{
          if ((rollpage == '') ||  isNaN(rollpage)){
            rollpage = 20;
          }
          if ((perPage == '') ||  isNaN(perPage)){
            perPage = 20;
          }
            var curPage = 0 ;
            var divisor = (currentPage - currentPage%rollpage)/rollpage;
            if ( currentPage%rollpage == 0){
                divisor = divisor - 1;
            }
            var preRollPage = ( divisor - 1 ) * rollpage + 1;
            var nextRollPage = ( divisor + 1 ) * rollpage + 1;
            var txt = "";
            txt = txt + "<table width=\"100%\" class=\"pagination\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";
            txt = txt + "<tr>";
            txt = txt + "<td align=\"left\" width=\"25%\">";
            if ( totalCount <= 0 ){
               txt = txt + 0 ;
            }else{
               txt = txt + ((currentPage - 1)*perPage + 1);
            }
            txt = txt + " - ";
            if ((currentPage*perPage) > totalCount ){
                txt = txt + totalCount;
            }else{
                txt = txt + (currentPage*perPage);
            }
            txt = txt + " 共 " + totalCount + " 条 " + totalPage + " 页";
            txt = txt + "</td>";
            txt = txt + "<td align=\"right\">";
            if (preRollPage > 0){
                txt = txt + "<a href=\"#?page=1\"><img src=\""+ imgUrlPrex + "/arrow4-1.gif\" border=\"0\" alt=\"首页\" onclick=\"findObj('" + pageControl + "').value=1; " + frmName + ".submit(); \"></img></a>";
                txt = txt + "&nbsp;";
                txt = txt + "<a href=\"#?page=" + preRollPage + "\"><img src=\""+ imgUrlPrex + "/arrow3-1.gif\" border=\"0\" alt=\"前一页\" onclick=\"findObj('" + pageControl + "').value="+preRollPage+"; " + frmName + ".submit(); \"></img></a>";
            }else{
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow4-1.gif\" border=\"0\" alt=\"首页\"></img>";
                txt = txt + "&nbsp;";
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow3-1.gif\" border=\"0\" alt=\"前一页\"></img>";
            }
            txt = txt + "&nbsp;";
            for(i = 1; i <= rollpage; i++){
                curPage = rollpage*divisor + i;
                if (curPage <= totalPage){
                    if (curPage != currentPage){
                        txt = txt + "<a href=\"#?page=" +curPage+ "\" onclick=\"findObj('" + pageControl + "').value=" +curPage+ "; " + frmName + ".submit(); \">" +curPage+ "</a>";
                        txt = txt + "&nbsp;";
                    }else{
                        txt = txt + "<strong>" + curPage + "</strong>";
                        txt = txt + "&nbsp;";
                    }
                }
            }
            if ( nextRollPage <= totalPage ){
                txt = txt + "<a href=\"#?page=" + nextRollPage + "\" onclick=\"findObj('" + pageControl + "').value=" +nextRollPage+ "; " + frmName + ".submit(); \"><img src=\""+ imgUrlPrex + "/arrow1-1.gif\" border=\"0\" alt=\"后一页\"></img></a>";
                txt = txt + "&nbsp;";
                txt = txt + "<a href=\"#?page=" + totalPage + "\" onclick=\"findObj('" + pageControl + "').value=" +totalPage+ "; " + frmName + ".submit(); \"><img src=\""+ imgUrlPrex + "/arrow2-1.gif\" border=\"0\" alt=\"尾页\"></img></a>";
            }else{
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow1-1.gif\" border=\"0\" alt=\"后一页\"></img>";
                txt = txt + "&nbsp;";
                txt = txt + "<img src=\""+ imgUrlPrex + "/arrow2-1.gif\" border=\"0\" alt=\"尾页\"></img>";
            }
            txt = txt + "</td>";
            txt = txt + "</tr>";
            txt = txt + "</table>";
            document.write(txt);
            //alert(txt);
        }catch(e){
        }
    }
    
    
    function trim(str){
        return str.replace(/^\s*|\s*$/g,"");
    }
  // for textarea maxLength check by luohua 2005.3.31  
   function fnOnKeyPress() 
   { 
       var obj = window.event.srcElement; 
       if  (obj.value.length >= obj.maxLength) 
           event.returnValue = false;           
   } 
    
   function fnOnPaste() 
   { 
       var obj = window.event.srcElement; 
       var maxLen = obj.maxLength; 
       var curLen = obj.value.length; 
       if (curLen >= maxLen) 
       { 
           event.returnValue = false; 
           return; 
       } 
       var newText = window.clipboardData.getData("text"); 
       if (newText.length + curLen <=maxLen ) 
       { 
           event.returnValue = true; 
           return; 
       } 
       event.returnValue = false; 
       if (obj.enableTruncate ) 
       { 
           obj.value = obj.value + newText.substring(0,maxLen-curLen); 
       } 
   } 
    
   function fnSetMaxLength(objName, maxLen, enableTruncate) 
   { 
       var obj = document.all(objName); 
       obj.maxLength = maxLen; 
       obj.enableTruncate = enableTruncate; 
       obj.onkeypress = fnOnKeyPress; 
       obj.onpaste = fnOnPaste; 
   }
   
   function popMarkTip(popTip, obj){
		var tip = document.getElementById('markTip');
        obj.onmousemove=function(e){
        	var mouse = self.mousePosition(e);
			tip.style.left = mouse.x + 10 + 'px'; 
            tip.style.top = mouse.y + 10 + 'px';
            tip.style.display = 'block';
            tip.innerHTML = popTip;
        } 
        obj.onmouseout=function(){ 
        	tip.style.display = 'none'; 
        } 
	}
   
	function mousePosition(e){ 
		var x,y; 
		var e = e||window.event; 
		return { 
			x:e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft, 
			y:e.clientY+document.body.scrollTop+document.documentElement.scrollTop 
		}; 
	}
	
	function hideMarkTip(){
		document.getElementById('markTip').style.display = 'none';
	}
	
