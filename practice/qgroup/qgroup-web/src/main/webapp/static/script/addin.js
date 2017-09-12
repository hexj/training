var oldStatus;
function dial(sNumber)
{
	//传入电话号码
	//改变windows的状态文字来通知应用程序需要外拨
	//用Outdial来通知需要外拨
	//也可以定义其他文字通知需要做别的事情
	window.alibaba_outdialnum.value=sNumber;
	oldStatus = window.status;
	window.status = "Alibaba_Outdial";
	window.status = oldStatus;
}

function showme(element)
{
	//element中定义了3个参数param1,param2,param3
	//在这里写入要调用的业务脚本方法，并把需要的参数传递进入
	func_Show(element.param1,element.param2,element.param3);
}

//Console页面里面获取得用户信息后请调用用户信息
//参数
//aid	此员工在呼叫系统中的工号
//apwd	员工在呼叫系统中的密码可以为空
//name	员工的真实名字
//callright	座席的权限 1：普通席，2：班长席，需要将权限细分时使用此字段扩充
//popupurl	来电弹出窗口时基础URL
//translist	转接时查询可以转接到的对象列表
//listenlist	监听时查询可以监听的对象列表，若没有监听权限，则为空
//Limitcountlist	查询最低在线人数当天列表，在列表之外均认为无需控制。如果某DIVISION无此需求返回值中为空
function   SetUserInfo(aid,apwd,name,callright,popupurl,translist,listenlist,limitcountlist)
{
	window.alibaba_userinfo.AgentID=aid;
	window.alibaba_userinfo.Password=apwd;
	window.alibaba_userinfo.AgentName=name;
	window.alibaba_userinfo.CallRight=callright;
	window.alibaba_userinfo.PopupURL =popupurl;
	window.alibaba_userinfo.TransList=translist;
	window.alibaba_userinfo.ListenList=listenlist;
	window.alibaba_userinfo.LimitCountList=limitcountlist;
	oldStatus = window.status;
	window.status = "Alibaba_LogonSucc";
	window.status = oldStatus;
}

function Logout()
{
	//alert(1);
	oldStatus = window.status;
	window.status = "Alibaba_Logout";
	window.status = oldStatus;
}

document.write("<input type='button' style='visibility:hidden;' name='alibaba_agent' value='按钮' onclick='showme(this)' param1='' param2='' param3=''>");
document.write("<input type='hidden' name='alibaba_outdialnum' value='' >");
document.write("<input type='hidden' name='alibaba_userinfo' value='' AgentID='' PopupURL='' LimitCountList='' ListenList='' TransList='' CallRight='' Password='' AgentName='' CallRight=''>");

