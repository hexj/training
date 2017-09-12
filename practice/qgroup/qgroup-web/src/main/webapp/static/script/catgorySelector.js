/* @describe ��������һ���ļ���Ŀѡ���
 * @example listOnlineAuction.vm��2007-9-11�� 927-939��
 * @author xiaoma
 * @date 2007-9-10
 */
CategorySelector = new function() {

	/**
		�����ʽ��������� 
	*/
	var _categoryMap = {};
	/**
	 *  ��̬�仯ָ��<select>��ѡ�����Ϊparent=id����������Ŀ
	 */
	var _addOptions = function(sel, id) {
		var catObj = _categoryMap[id];
		if (catObj && catObj.children) {
			if (catObj.children.length > 0) {
				sel.options[0] = new Option('', '');
			}
			for (var i = 0, len = catObj.children.length; i < len; i++) {
				var cid = catObj.children[i];
				
				var catName=_categoryMap[cid]['name'];
				if(!catName)
					continue;
				sel.options[sel.options.length] = new Option(_categoryMap[cid]['name'], _categoryMap[cid]['id']);
			}
		}
	}

	/**
	 * ��ʼ��ԭ����ʽ�������Ŀ���ݣ�
	 �� ÿ����Ŀ��ʽ��ΪJSON {name:$name, id:$id, parent:$pid, children:[]}
	 */
	var _categoryCache = {};
	this.initData = function(data) {
		/* ��Ŀ¼ */
		_categoryMap['0'] = {name:'root', id:'0', children:[]};
		for (var id in data) {
			var d = data[id], pid=d[1] || '0';
					
			/* ����ȴ��������Ŀ������Ŀ��������Ѵ��� */
			if (_categoryMap[id]) {
				_categoryMap[id].name = d[0] += ' ->';
				_categoryMap[id].parent = pid;
			}
			else {
				_categoryMap[id] = {name:d[0], id:id, parent:pid};
			}
			
			if (_categoryMap[pid]) {/* �������Ŀ�����Ѵ��� */
				if (!_categoryMap[pid]['children']) {
					_categoryMap[pid]['children'] = [];
					_categoryMap[pid]['name'] += ' ->';
				}
			} else { /* �������Ŀ���󲻴��ڣ��ȴ���һ�� */
				_categoryMap[pid] = {name:'unknown', id:pid, children:[]};
			}
			_categoryMap[pid]['children'].push(id); 			
		}
	}

	/** 
	 * ����4��<select>��name��Ϊ����
	 * sel0, sel1, sel2, sel3�ֱ��Ӧ�ļ���ĿĿ¼
	 */
	this.attach = function(sel0, sel1, sel2, sel3, config) {
		var selGroup = [sel0, sel1, sel2, sel3];
		config = config || {};
		var handle = {};
		
		/**
		 * ��ʼ��ѡ��ĳ����Ŀ������Ϊ��ĩ�����Ŀid
		 */
		handle.initCategory = function(id) {

			if(id==''||id==undefined||id=='undefined'||id==null||id=='null'){//���ID�Ƿ����򷵻�
				return;
			}
			var catPath = [];
			var catObj = _categoryMap[id];
			catPath.push(id);
			//�ݹ����ϲ�����Ŀ·������id���浽catPath������
			while (catObj.parent != '0') {
				catPath.unshift(catObj.parent);
				catObj = _categoryMap[catObj.parent];
			}

			selGroup[0].value = catPath[0];
			var i;
			
			for (i = 1; i < catPath.length; i++) {
				_addOptions(selGroup[i], catPath[i-1]);
				selGroup[i].value = catPath[i];
				if(selGroup[i].style&&selGroup[i].style.display){
					selGroup[i].style.display='';
				}
			}
			_addOptions(selGroup[i], catPath[i-1]);//add by shihong 2007-10-9
		}
		
		/**
		 * ���ѡ�����Ŀ����·��
		 */
		handle.getPath = function() {
			var vals = [];
			for (var i = 0; i < selGroup.length; ++i) {
				if (selGroup[i].value) {
					vals.push(selGroup[i].options[selGroup[i].selectedIndex].text.replace(/\s\-\>/,''));
				}else {
					break;
				}
			}
			return vals.join(' >> ');
		}
		/**
		 * ע��ÿ��<select>�� onchange �¼���
		 */
		for (var i = 0; i < selGroup.length; i++) {
			selGroup[i].onchange = function(idx, callback) {
				return function() {
					for (var j = idx+1; j < selGroup.length; j++) {//����Ӽ���ĿĿ¼
						selGroup[j].options.length = 0;
						if(selGroup[j].style){
							selGroup[j].style.display='none';
						}
					}
					var sel = selGroup[idx];
					var pid = sel.value;
					//��ʼ���Ӽ���ĿĿ¼
					var nextSel = selGroup[idx+1];
					if (nextSel){
						if(_categoryMap[pid]&& _categoryMap[pid].children) {
							if(nextSel.style){
								nextSel.style.display='';
							}
							_addOptions(nextSel, sel.value);
						}
						else{
							if(nextSel.style){
								nextSel.style.display='none';
							}
						}
					}
					//��ȡѡ�����Ŀid,��Ϊ����ȡ��һ������Ŀid  add by shihong 2007-10-9 
					if (sel.value==''&&idx>0){
						
						//�����¼��б�
						for(var k = idx;k<selGroup.length;k++){
							var hiddenSel = selGroup[k+1];
							if(hiddenSel&&hiddenSel.style){
								hiddenSel.style.display='none';
							}
						}
						
						sel = selGroup[idx-1];
					}
					if (config.onChange) {
						config.onChange.call(sel, sel);
					}
				}
			}(i);
		}

		/**
		 * Ĭ�ϳ�ʼ��һ��Ŀ¼
		 */
		selGroup[0].options[0] = new Option('', '');
		_addOptions(selGroup[0], '0');

		return handle;
	}

}