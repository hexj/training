var oldStatus;
function dial(sNumber)
{
	//����绰����
	//�ı�windows��״̬������֪ͨӦ�ó�����Ҫ�Ⲧ
	//��Outdial��֪ͨ��Ҫ�Ⲧ
	//Ҳ���Զ�����������֪ͨ��Ҫ���������
	window.alibaba_outdialnum.value=sNumber;
	oldStatus = window.status;
	window.status = "Alibaba_Outdial";
	window.status = oldStatus;
}

function showme(element)
{
	//element�ж�����3������param1,param2,param3
	//������д��Ҫ���õ�ҵ��ű�������������Ҫ�Ĳ������ݽ���
	func_Show(element.param1,element.param2,element.param3);
}

//Consoleҳ�������ȡ���û���Ϣ��������û���Ϣ
//����
//aid	��Ա���ں���ϵͳ�еĹ���
//apwd	Ա���ں���ϵͳ�е��������Ϊ��
//name	Ա������ʵ����
//callright	��ϯ��Ȩ�� 1����ͨϯ��2���೤ϯ����Ҫ��Ȩ��ϸ��ʱʹ�ô��ֶ�����
//popupurl	���絯������ʱ����URL
//translist	ת��ʱ��ѯ����ת�ӵ��Ķ����б�
//listenlist	����ʱ��ѯ���Լ����Ķ����б���û�м���Ȩ�ޣ���Ϊ��
//Limitcountlist	��ѯ����������������б����б�֮�����Ϊ������ơ����ĳDIVISION�޴����󷵻�ֵ��Ϊ��
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

document.write("<input type='button' style='visibility:hidden;' name='alibaba_agent' value='��ť' onclick='showme(this)' param1='' param2='' param3=''>");
document.write("<input type='hidden' name='alibaba_outdialnum' value='' >");
document.write("<input type='hidden' name='alibaba_userinfo' value='' AgentID='' PopupURL='' LimitCountList='' ListenList='' TransList='' CallRight='' Password='' AgentName='' CallRight=''>");

