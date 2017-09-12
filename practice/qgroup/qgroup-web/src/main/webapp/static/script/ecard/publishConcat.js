TB.namespace('ark.publish');
TB.ark.publish.attrConcat =(function() {
	return {
		init: function() {
			var attrInfo = $D.get('ark:itemInfo'); //�����Ʒ������Ϣ��������
			if(!attrInfo) return; //�����������Ʒ������Ϣ������������������
			var attrList = $D.getElementsByClassName('ark:itemAttr','tr',attrInfo); //�����Ʒ������Ϣ��������Ʒ���Ե�tr
			if(!attrList) return; //�����������Ʒ���ԣ���������
			var attrInputUtil = []; //����һ�������������ÿһ�����Ե�����checkbox����[���Ե�����checkbox,���Ե�����checkbox]
			var attrInputAll = [];	//����һ����������������е�checkbox(��ȫѡ��checkbox)
			
			//���attrInputUtil����
			for(var i = 0; i < attrList.length; i++) {
				var attr = attrList[i].getElementsByTagName('ul')[0].getElementsByTagName('input');
				attrInputUtil.push(attr);
			}
			
			//���attrInputAll����
			for(var i = 0; i < attrInputUtil.length; i++){
				for (var j = 0; j < attrInputUtil[i].length; j++ ){
					attrInputAll.push(attrInputUtil[i][j]);
				}	
			}
			
			var attrTable = $D.get('ark:attrUtil'); //��ô��������Եı��
			var attrUtil = attrTable.getElementsByTagName('tbody')[0].getElementsByTagName('tr');  //��ô��������Եı���tr
			
			var inputDefault = function(root) {
				var inputList = root.getElementsByTagName('input');
				for(var a = 0 ; a < inputList.length ; a++) {
					inputList[a].value='';
				}	
			}
		
			
			//��������ѡʱִ�еĺ���
			var checkShow = function(checkbox) {
				
				var attrConcat = []; //����һ�����������б�ѡ�е�checkbox�Ĳ���ֵ����ʽͬattrInputUtil
				
				//��������Ƿ���ȫѡ��ť������ǣ�������Ե�����checkbox����ѡ��
				if(checkbox) {
					var thisArray = checkbox.parentNode.parentNode.getElementsByTagName('input');
					if(checkbox == thisArray[thisArray.length-1]) {			
						for (var i = 0; i < thisArray.length-1; i++ ){
						  thisArray[i].checked = true;
						}
					}
				}
				
				//����attrInputUtil���飬��������Ե�����checkboxû��һ����ѡ�У��������������
				for(var i = 0; i < attrInputUtil.length; i++){
					var attrInputList = attrInputUtil[i]; 
					
					var attrInputChecked =[];
					var attrValueList = [];
					
					//�������������checkbox��checked���ԣ�����ֵ������������ӵ�attrInputChecked������,�����ѡ��ȡ��b2c:params�����ŵ�attrValueList������
					for (var j = 0; j < attrInputList.length-1; j++ ){
						attrInputChecked.push(attrInputList[j].checked);
						
						if(attrInputList[j].checked){
							var attrValue = attrInputList[j].getAttribute('ark:params');
							attrValueList.push(attrValue);
						}
					}	
					
					attrConcat.push(attrValueList);
					
					var checkedString = attrInputChecked.join(''); //��attrInputChecked����ת�����ַ���		
					if(checkedString.indexOf('true')==-1) return;  //���δ��ѡ�еģ��������˺���
				}
				
				
				//������attrConcatת�����ַ���attrConcatString
				var attrConcatString = attrConcat.join(',');
				
				
				//�������д��������Եı���tr�����������������ȫ�������ʾ��tr����������������
				for (var i = 0; i < attrUtil.length; i ++ ) {		
					var attrValue = attrUtil[i].getAttribute('ark:params'); //���������Եı���tr�е�b2c:params����
					var attrValues = attrValue.split('&'); //����õĲ�������'&'�ָ�������attrValues
					if(attrValues.length > 2) {
						attrValues.pop();
					}
					var ishas = true;	
					
					//��������attrValues���ж�attrConcatString�Ƿ��д������еĶ�������������������˱���
					for (var j = 0; j < attrValues.length; j++){ 
						if(ishas) {
							if(attrConcatString.indexOf(attrValues[j])==-1){
								ishas = false;
								break;
							}
						}
					} 
					
					
					//�����ȫ�������򽫴˴��������Եı���tr��ʾ����֮����
					if(ishas){
						attrTable.style.display='';
						attrUtil[i].style.display='';
					} else {
						attrUtil[i].style.display='none';	
					}
				}	
			}
			
			//������ȡ��ѡȡʱִ�еĺ���
			var checkHidden = function(checkbox){	
			
				//��������Ƿ���ȫѡ��ť������ǣ�������Ե�����checkbox����ȡ��ѡ�У������б����أ������˺���
				
				var thisArray = checkbox.parentNode.parentNode.getElementsByTagName('input');
				if(checkbox == thisArray[thisArray.length-1]) {			
					if(confirm('��ȷ��Ҫȡ����ȫ��������ȡ���󽫵����������ʾ�б���ʧ��')) {
						for (var i = 0; i < thisArray.length-1; i++ ){
							thisArray[i].checked = false;
						}	
						attrTable.style.display='none';	
						inputDefault(attrTable);
						return;
					} else {
						checkbox.checked = true;
					}
					
				} else {//���������ȫѡ��ť��ȫѡ��ťΪ��ѡ��
					thisArray[thisArray.length-1].checked = false;
					
				}
				
					
				var attrValue = checkbox.getAttribute('ark:params');
				for (var i = 0; i < attrUtil.length; i ++ ) {
					var attr = attrUtil[i].getAttribute('ark:params');
					
					if(attr.indexOf(attrValue)>-1){
						attrUtil[i].style.display='none';
						inputDefault(attrUtil[i]);
					}
					
				}
				
				//���û�������б������������б�ͷ
				var attrStyle = $D.getStyle(attrUtil,'display');
				var attrString = attrStyle.join('');
				if(attrString.indexOf('block')>-1||attrString.indexOf('table-row')>-1){
					attrTable.style.display='';
				} else {
					attrTable.style.display='none';
				}
			}
			
			
			//����¼�
			$E.on(attrInputAll,'click',function(){
				if(this.checked) {
					checkShow(this);
				} else {
					checkHidden(this);
				}
			})
			checkShow();
		}
	}
})()


TB.ark.publish.valueConcat =(function() {
	return {
		init: function() {
			var attrUtil = $D.get('ark:attrUtil');
			if(!attrUtil) return;
			
			var inputPrice = $D.get('ark:item-price');

			var checkValue = function() {
				var attrPrice = $D.getElementsByClassName('ark:price','input',attrUtil);
				for(var i = 0; i < attrPrice.length; i++) {
					if(attrPrice[i].value == '0' || attrPrice[i].value == '' ) {
						attrPrice[i].value = inputPrice.value;
					}
				}
			}
			
			var isPriceValid = function(sText) {
				var rePrice = /^\d*\.?\d{0,2}$/;
				return rePrice.test(sText);
			}
			$E.on(inputPrice,'change',function(){	
				if(this.value != '' && this.value != '0' && isPriceValid(this.value)) {
					checkValue();
				}
			})  
			
			
			var itemAmount = $D.get('ark:itemAmount');
			var attrPiece = $D.getElementsByClassName('ark:piece','input',attrUtil);
			var isPieceValid = function(sText) {
				var rePiece = /^\d*$/;
				return rePiece.test(sText);
			}
			var getItemAmount = function() {
				var itemAmountValue = 0;
				for(var i = 0; i < attrPiece.length; i++) {
					if(attrPiece[i].value != '' && attrPiece[i].value != '0' && isPriceValid(attrPiece[i].value) ) {
						itemAmountValue += parseInt(attrPiece[i].value,10);
					}
				}
				itemAmount.innerHTML = itemAmountValue;
			}
			
			$E.on(attrPiece,'change',function(){
				getItemAmount();
			})
			$E.onContentReady('ark:attrUtil',function(){
				getItemAmount();
			})
			
			var attrInfo = $D.get('ark:itemInfo');
			var attrList = $D.getElementsByClassName('ark:itemAttr','tr',attrInfo);
			var inputLists = []
			for(var i = 0; i < attrList.length; i ++) {
				var inputList = attrList[i].getElementsByTagName('input'); 	
				inputLists.push(inputList);
			}
			
			$E.on(inputLists,'click', function(){	
				if(!this.checked) {					
					setTimeout(function(){
						getItemAmount();
					},1);	
				}
			})
			
			
			var shopCats = $D.get('ark:shopCats');
			var shopCatsText = $D.get('ark:shopCatsText');
			var checkboxs = shopCats.getElementsByTagName('input');
			
			
			var shopCatsTextValue = function() {
				var checkboxChecked = [] ;
				var checkboxCheckedText;
				for(i = 0; i < checkboxs.length; i++ ){
					if(checkboxs[i].checked) {
						var thisId = checkboxs[i].getAttribute('id');
						var thisIdNo = thisId.split('_')[1];
						checkboxChecked.push(thisIdNo);
					}
				}
				checkboxCheckedText = checkboxChecked.join(',');
				checkboxCheckedText = ',' + checkboxCheckedText + ',';
				shopCatsText.value = checkboxCheckedText;
			}
			
			$E.on(checkboxs,'click',function(){
				shopCatsTextValue();
			
			})
		}
	}
})()

$E.onContentReady('ark:itemInfo', function(){
	TB.ark.publish.attrConcat.init();
	TB.ark.publish.valueConcat.init();
})