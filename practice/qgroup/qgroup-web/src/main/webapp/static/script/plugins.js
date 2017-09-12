/*
*alahan 20050128 modify find object method
*/
function fOnChange(y,m,d) {
    return false;
}
function fStartPop(startc,endc) {
  //try{
  var sd=fParseDate(endc.value); 
  if (!sd) sd=gEnd;
  fPopCalendar(startc, [gBegin,sd,sd]);
  //}catch(e){}
}
function fEndPop(startc,endc) {
  //try{
  var sd=fParseDate(startc.value);
  if (!sd) sd=gBegin; 
  fPopCalendar(endc, [sd,gEnd,sd]);
  //}catch(e){}
}

function fAfterSelected(y,m,d) {
    //try{
    if (gdCtrl.name.length>4) {
        var baseName = gdCtrl.name.substring(4);
        //var hideObj = eval("gdCtrl.form." + baseName);
        var hideObj = findObj(baseName);
        //var HourObj = eval("gdCtrl.form.time_" + baseName);
        var HourObj = findObj("time_" + baseName);
        if(gdCtrl.value == '') {
            hideObj.value = '';
        } else {            
            if (HourObj && HourObj.selectedIndex>=0) 
                hideObj.value = gdCtrl.value+' '+HourObj.options[HourObj.selectedIndex].value;
            else 
                hideObj.value = gdCtrl.value;
        }
    }
    //}catch(e){}
}
function fPopDepart(dc1) {
    //try{
    var sd=fParseDate(dc1.value);
    var range=(sd+''==[2001,6,6]+'')?[[2001,6,10],[2001,7,20],sd,[2001,6,13]]:[];
    fPopCalendar(dc1, range);
  //}catch(e){}
}
function fPopClear() {
    //try{
    if (gdCtrl.name.length>4) {
        gdCtrl.value = '';
        var baseName = gdCtrl.name.substring(4);
        //var hideObj = eval("gdCtrl.form." + baseName);
        var hideObj = findObj(baseName);
        hideObj.value = '';
        //var HourObj = eval("gdCtrl.form.time_" + baseName);
        var HourObj = findObj("time_" + baseName);
        if (HourObj) 
             HourObj.options.selectedIndex = -1;
    }
    //}catch(e){}
}
function findObj(controlName) { //v1.01
        var control;
        try{
            control = parent.document.getElementsByName(controlName)[0];
            return control;
        }catch(e){
            return null;
        }
    }