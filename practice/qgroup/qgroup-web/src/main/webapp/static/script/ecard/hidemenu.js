// JavaScript Document ВЫЕЅвўВи
var lastSelectedTab;
function emailTabSelect(id,ele){
	var ids = new Array('SchemeReplyMail','SchemePhone','SchemeAlipay','SchemeTransfer','SchemeRemark','SchemeViewStepList','OffBargain0','OffBargain1','OffBargain2','OffBargain3','OffBargain4','OffBargain5','OffBargain6','OffBargain7','OffBargain8','OffBargain9','OffBargain10','OffBargain11','OrderView','UserWorkLog','Service','MallProductInfo','UserAdsInfo');
	for(var i=0;i<ids.length;i++){
		if(document.getElementById(ids[i])){
				if(ids[i] == id){
					document.getElementById(ids[i]).style.display = '';
				}else{
					document.getElementById(ids[i]).style.display = 'none';
				}
		}
	}
	ele.className = 'SelectMenu';
	if(lastSelectedTab && lastSelectedTab != ele) lastSelectedTab.className = '';
	lastSelectedTab = ele;
}
var lastSelected2Tab;
function emailTabSelect2(id,ele){
	var ids2 = new Array('RecordNew','RecordMessage','RecordPhone','RecordPunish','RecordRefund');
	for(var i=0;i<ids2.length;i++){
		if(document.getElementById(ids2[i])){
				if(ids2[i] == id){
					document.getElementById(ids2[i]).style.display = '';
				}else{
					document.getElementById(ids2[i]).style.display = 'none';
				}
		}
	}
	ele.className = 'SelectMenu';
	if(lastSelected2Tab && lastSelected2Tab != ele) lastSelected2Tab.className = '';
	lastSelected2Tab = ele;
}

