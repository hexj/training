var mIsinitialized = false;
function Init() {
    var location=window.location;
    var pathname=document.location.pathname+"";
    var module=pathname.substring(1,pathname.length);
    var httptype = location.protocol.slice(0,-1);
    if(httptype =='https'){
    	var page="https://"+location.host+"/"+"static/script/ipopeng.html";
    }else{
    	var page="http://"+location.host+"/"+"static/script/ipopeng.html";
    }
    if(!mIsinitialized) {
        document.write('<iframe width=181 height=177 name="gToday:normal:normal.js" id="gToday:normal:normal.js" src="'+page+'" scrolling="no" frameborder="0" style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;"></iframe>');
        document.write('<style type="text/css">\n.plain {height:20; border:1px solid silver; color:blue; text-align:center;}\n.vbtn {height:20; width:18; color:blue; text-align:center; border:1px solid silver;}\n.timebox {height:20; border:1px solid silver; color:blue; font:9pt arial;}\n</style>');
        mIsinitialized = true;
    }
}
function CreateDate(strName, sDate, isShowHour)
{
  Init();
    var strDefaultDate=sDate.replace(/[\/ :]/g,"-");
    var vArr=strDefaultDate.split("-");
    var strDefaultDay = "";
    var strDefaultTime = "";
    var strDefaultHour=0;
    var strDefaultMint=0;
    var txt = "";
    if(vArr.length>=3) {
        strDefaultDay = vArr[0] + "-" + vArr[1] + "-" + vArr[2];
        strDefaultTime = strDefaultDay;
    }
    if(vArr.length>=4 && !isNaN(parseInt(vArr[3], 10))) {
        strDefaultHour = parseInt(vArr[3], 10);
        strDefaultMint = parseInt(vArr[4], 10);
        //strDefaultTime += ' ' + strDefaultHour + ":00";
        strDefaultTime += ' ' + (strDefaultHour<10?"0":"") +strDefaultHour+ ":"+strDefaultMint+":00";
    }
    txt = '<input name=\"gdc_'+strName+'\" value="'+strDefaultDay+'" class=\"plain\" size=\"10\" onfocus=\"this.blur();\" readonly><INPUT name=\"popcal\" onchange=\"findObj(\''+strName+'\').value=this.value; alert(findObj(\''+strName+'\').value);"\onclick=\"if(this.blur) this.blur();var fm=this.form;gfPop.fPopDepart(findObj(\'gdc_'+strName+'\'));\" type=\"Button\" value=\"¨‹\" class=\"vbtn\">';
    if(isShowHour) {
        //document.write("<select name='time_"+strName+"' class='timebox' onchange='if(findObj('gdc_"+strName+"').value!=\"\"){findObj('"+strName+"').value=findObj('gdc_"+strName+"').value+\" \"+this.options[this.selectedIndex].value}'>");
        //document.write("\n<option value=''></option>");
        //txt = txt + '<select name=\"time_'+strName+'\" class=\"timebox\" onchange="if(findObj(\'gdc_'+strName+'\').value!=\'\'){findObj(\''+strName+'\').value=findObj(\'gdc_'+strName+'\').value+\' \'+this.options[this.selectedIndex].value\"}>';
        txt = txt + '<select name=\"time_'+strName+'\" class=\"timebox\" onchange="if(findObj(\'gdc_'+strName+'\').value!=\'\'){findObj(\''+strName+'\').value=findObj(\'gdc_'+strName+'\').value+ \' \'+this.options[this.selectedIndex].value}">';
        txt = txt + '\n<option value=""></option>';
        for(var i=0;i<24;i++) {
            //document.write("\n<option value='"+(i<10?"0":"")+i+":00' "+((i==strDefaultHour&&strDefaultDay!='')?"selected":"")+">"+(i<10?"0":"")+i+":00</option>");
            for(var j=0;j<2;j++){
            	txt = txt + '\n<option value=\''+(i<10?"0":"")+i+":"+(j<1?"00":"30")+":00' "+((i==strDefaultHour&&strDefaultDay!=''&&strDefaultMint==j*30)?"selected":"")+'>'+(i<10?"0":"")+i+':'+(j<1?"00":"30")+':00</option>';
            //txt = txt + '\n<option value=\''+(i<10?"0":"")+i+":30:00' "+((i==strDefaultHour&&strDefaultDay!='')?"selected":"")+'>'+(i<10?"0":"")+i+':30:00</option>';
            }
        }
        //document.write("</select>");
        txt = txt + '</select>';
    }
    //document.write('<input type=hidden size=18 name="'+strName+'" value="'+strDefaultTime+'">');
    txt = txt + '<input type=hidden size=18 name="'+strName+'" value="'+strDefaultTime+'">';
    document.write(txt);
    //alert(txt);
}
    function findObj(controlName) { //v1.01
        var control;
        try{
            control = document.getElementsByName(controlName)[0];
            return control;
        }catch(e){
            return null;
        }
    }
