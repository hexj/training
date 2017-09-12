/* @describe 用来定义一个四级类目选择框
 * @example listOnlineAuction.vm（2007-9-11） 927-939行
 * @author xiaoma
 * @date 2007-9-10
 */
CategorySelector = new function() {

	/**
		保存格式化后的数据 
	*/
	var _categoryMap = {};
	/**
	 *  动态变化指定<select>的选项，数据为parent=id的所有子类目
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
	 * 初始化原来格式输出的类目数据，
	 × 每个类目格式化为JSON {name:$name, id:$id, parent:$pid, children:[]}
	 */
	var _categoryCache = {};
	this.initData = function(data) {
		/* 根目录 */
		_categoryMap['0'] = {name:'root', id:'0', children:[]};
		for (var id in data) {
			var d = data[id], pid=d[1] || '0';
					
			/* 如果先处理过子类目，该类目对象可能已存在 */
			if (_categoryMap[id]) {
				_categoryMap[id].name = d[0] += ' ->';
				_categoryMap[id].parent = pid;
			}
			else {
				_categoryMap[id] = {name:d[0], id:id, parent:pid};
			}
			
			if (_categoryMap[pid]) {/* 如果父类目对象已存在 */
				if (!_categoryMap[pid]['children']) {
					_categoryMap[pid]['children'] = [];
					_categoryMap[pid]['name'] += ' ->';
				}
			} else { /* 如果父类目对象不存在，先创建一个 */
				_categoryMap[pid] = {name:'unknown', id:pid, children:[]};
			}
			_categoryMap[pid]['children'].push(id); 			
		}
	}

	/** 
	 * 传递4个<select>的name作为参数
	 * sel0, sel1, sel2, sel3分别对应四级类目目录
	 */
	this.attach = function(sel0, sel1, sel2, sel3, config) {
		var selGroup = [sel0, sel1, sel2, sel3];
		config = config || {};
		var handle = {};
		
		/**
		 * 初始化选择某个类目，参数为最末层的类目id
		 */
		handle.initCategory = function(id) {

			if(id==''||id==undefined||id=='undefined'||id==null||id=='null'){//如果ID非法，则返回
				return;
			}
			var catPath = [];
			var catObj = _categoryMap[id];
			catPath.push(id);
			//递归向上查找类目路径，将id保存到catPath数组中
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
		 * 输出选择的类目名字路径
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
		 * 注册每个<select>的 onchange 事件。
		 */
		for (var i = 0; i < selGroup.length; i++) {
			selGroup[i].onchange = function(idx, callback) {
				return function() {
					for (var j = idx+1; j < selGroup.length; j++) {//清空子级类目目录
						selGroup[j].options.length = 0;
						if(selGroup[j].style){
							selGroup[j].style.display='none';
						}
					}
					var sel = selGroup[idx];
					var pid = sel.value;
					//初始化子级类目目录
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
					//获取选择的类目id,若为空则取上一级别类目id  add by shihong 2007-10-9 
					if (sel.value==''&&idx>0){
						
						//隐藏下级列表
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
		 * 默认初始化一级目录
		 */
		selGroup[0].options[0] = new Option('', '');
		_addOptions(selGroup[0], '0');

		return handle;
	}

}