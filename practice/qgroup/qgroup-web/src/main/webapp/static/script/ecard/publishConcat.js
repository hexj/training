TB.namespace('ark.publish');
TB.ark.publish.attrConcat =(function() {
	return {
		init: function() {
			var attrInfo = $D.get('ark:itemInfo'); //获得商品基本信息的整个表单
			if(!attrInfo) return; //如果不存在商品基本信息的整个表单，跳出程序
			var attrList = $D.getElementsByClassName('ark:itemAttr','tr',attrInfo); //获得商品基本信息中所有商品属性的tr
			if(!attrList) return; //如果不存在商品属性，跳出程序
			var attrInputUtil = []; //命名一个数组用来存放每一个属性的所有checkbox，如[属性的所有checkbox,属性的所有checkbox]
			var attrInputAll = [];	//命名一个数组用来存放所有的checkbox(除全选的checkbox)
			
			//获得attrInputUtil数组
			for(var i = 0; i < attrList.length; i++) {
				var attr = attrList[i].getElementsByTagName('ul')[0].getElementsByTagName('input');
				attrInputUtil.push(attr);
			}
			
			//获得attrInputAll数组
			for(var i = 0; i < attrInputUtil.length; i++){
				for (var j = 0; j < attrInputUtil[i].length; j++ ){
					attrInputAll.push(attrInputUtil[i][j]);
				}	
			}
			
			var attrTable = $D.get('ark:attrUtil'); //获得存放添加属性的表格
			var attrUtil = attrTable.getElementsByTagName('tbody')[0].getElementsByTagName('tr');  //获得存放添加属性的表格的tr
			
			var inputDefault = function(root) {
				var inputList = root.getElementsByTagName('input');
				for(var a = 0 ; a < inputList.length ; a++) {
					inputList[a].value='';
				}	
			}
		
			
			//定义点击被选时执行的函数
			var checkShow = function(checkbox) {
				
				var attrConcat = []; //命名一个数组存放所有被选中的checkbox的参数值，形式同attrInputUtil
				
				//检测点击的是否是全选按钮，如果是，则该属性的所有checkbox都被选中
				if(checkbox) {
					var thisArray = checkbox.parentNode.parentNode.getElementsByTagName('input');
					if(checkbox == thisArray[thisArray.length-1]) {			
						for (var i = 0; i < thisArray.length-1; i++ ){
						  thisArray[i].checked = true;
						}
					}
				}
				
				//遍历attrInputUtil数组，如果有属性的所有checkbox没有一个被选中，则跳出这个函数
				for(var i = 0; i < attrInputUtil.length; i++){
					var attrInputList = attrInputUtil[i]; 
					
					var attrInputChecked =[];
					var attrValueList = [];
					
					//获得属性下所有checkbox的checked属性（布尔值），并将其添加到attrInputChecked数组中,如果被选获取其b2c:params参数放到attrValueList数组中
					for (var j = 0; j < attrInputList.length-1; j++ ){
						attrInputChecked.push(attrInputList[j].checked);
						
						if(attrInputList[j].checked){
							var attrValue = attrInputList[j].getAttribute('ark:params');
							attrValueList.push(attrValue);
						}
					}	
					
					attrConcat.push(attrValueList);
					
					var checkedString = attrInputChecked.join(''); //将attrInputChecked数组转换成字符串		
					if(checkedString.indexOf('true')==-1) return;  //如果未有选中的，则跳出此函数
				}
				
				
				//将数组attrConcat转换成字符串attrConcatString
				var attrConcatString = attrConcat.join(',');
				
				
				//遍历所有存放添加属性的表格的tr，配对其参数，如果完全配对则显示此tr，如果不配对则隐藏
				for (var i = 0; i < attrUtil.length; i ++ ) {		
					var attrValue = attrUtil[i].getAttribute('ark:params'); //获得添加属性的表格的tr中的b2c:params参数
					var attrValues = attrValue.split('&'); //将获得的参数按照'&'分隔成数组attrValues
					if(attrValues.length > 2) {
						attrValues.pop();
					}
					var ishas = true;	
					
					//遍历数组attrValues，判断attrConcatString是否含有此数组中的对象，如果不包含则跳出此遍历
					for (var j = 0; j < attrValues.length; j++){ 
						if(ishas) {
							if(attrConcatString.indexOf(attrValues[j])==-1){
								ishas = false;
								break;
							}
						}
					} 
					
					
					//如果完全包含，则将此存放添加属性的表格的tr显示，反之隐藏
					if(ishas){
						attrTable.style.display='';
						attrUtil[i].style.display='';
					} else {
						attrUtil[i].style.display='none';	
					}
				}	
			}
			
			//定义点击取消选取时执行的函数
			var checkHidden = function(checkbox){	
			
				//检测点击的是否是全选按钮，如果是，则该属性的所有checkbox都被取消选中，属性列表隐藏，跳出此函数
				
				var thisArray = checkbox.parentNode.parentNode.getElementsByTagName('input');
				if(checkbox == thisArray[thisArray.length-1]) {			
					if(confirm('您确定要取消该全部属性吗，取消后将导致下面的显示列表消失？')) {
						for (var i = 0; i < thisArray.length-1; i++ ){
							thisArray[i].checked = false;
						}	
						attrTable.style.display='none';	
						inputDefault(attrTable);
						return;
					} else {
						checkbox.checked = true;
					}
					
				} else {//如果不是是全选按钮，全选按钮为非选中
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
				
				//如果没有属性列表，则隐藏属性列表头
				var attrStyle = $D.getStyle(attrUtil,'display');
				var attrString = attrStyle.join('');
				if(attrString.indexOf('block')>-1||attrString.indexOf('table-row')>-1){
					attrTable.style.display='';
				} else {
					attrTable.style.display='none';
				}
			}
			
			
			//添加事件
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