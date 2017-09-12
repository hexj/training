TB.namespace('app');
D = YAHOO.util.Dom;
E = YAHOO.util.Event;

TB.app.crm = function()
{

	var handle = new Object();
	return handle;
}();


TB.app.crm.CheckToggle = new function(){

	var _config = {
        contentUnselectedClass: 'hidden',
		contentSelectedClass: null,
		selectedAttr: 'checked',
		selectedClass: 'selected',
		unselectedClass: null
    }
	/**
	* 
	* @param {Object} checkbox Array
	*/
	//alert(this.prototype);
	this.attach = function(chkList, config){
	
		var current;
		var config;
		var handle = {};
		config = TB.applyIf(config || {}, _config);
        chkList.forEach(function(item){
				item.ctrlToggle = TB.app.crm.SimpleToggle.attach(item, config);				
				if(D.hasClass(item, config.selectedClass) || item[config.selectedAttr]){
					current = item;
					current.ctrlToggle.flag = true;
					//console.debug('current: ', current);
				}
				
				$E.on(item, 'click', function(ev){
					handle.selectItem(item);
					//console.debug(item);
					//可以在这里进行判断，以实现手风琴效果。
				/*	if(chkList.length > 1 && current == item){
						return;
					}
					
					if(chkList.length == 1)
					{
						item.ctrlToggle.toggleFun();
					}else{
						current.ctrlToggle.toggleFun();
						item.ctrlToggle.toggleFun();
						current = item;
					}*/
			});
		});
		handle.selectItem = function(item){
			if(chkList.length > 1 && current == item){
				return;
			}
			
			if(chkList.length == 1)
			{
				item.ctrlToggle.toggleFun();
			}else{
				current.ctrlToggle.toggleFun();
				item.ctrlToggle.toggleFun();
				current = item;
			}
		};
		return handle;
	};
	
}
TB.app.crm.SimpleToggle = new function(){
	
	this.attach=function(toggle, config, flag){
		handle ={};
		handle.flag = flag || false;
		handle.config = config;
		handle.toggle = toggle;
		//console.debug(toggle.getAttribute('crm:data'));
		handle.cont = $D.getElementsByClassName(toggle.getAttribute('crm:data'), 'div')[0];
		handle.toggleFun= function(){
			if(this.flag){
				 this.close();
				 this.flag = false;
			}else{
				 this.show();
				 if( this.config.afterShow != null)
				 {
					 //console.debug(this.cont);
					 this.config.afterShow.call(this, this.cont);
				 }
				 this.flag = true;
			}
		}
		handle.show =function(){
			//cont
			this.modifClass('remove', this.cont, config.contentUnselectedClass);
			this.modifClass('add', this.cont, config.contentSelectedClass);
			//toggle
			this.modifClass('remove', this.toggle, config.unselectedClass);
			this.modifClass('add', this.toggle, config.selectedClass);
		};
		handle.close =function(){
			//cont
			this.modifClass('remove', this.cont, config.contentSelectedClass);
			this.modifClass('add', this.cont, config.contentUnselectedClass);
			//toggle
			this.modifClass('remove', this.toggle, config.selectedClass);
			this.modifClass('add', this.toggle, config.unselectedClass);
		
		};
		handle.isShow =function(){
			return this.flag;
		}
		handle.modifClass = function(type, tar, className){
			if(type == 'remove'){
				if(className && D.hasClass(tar, className))
				{
				   D.removeClass(tar, className);
				}
			}else if(type == 'add'){
				if(className && !D.hasClass(tar, className))
				{
				   D.addClass(tar, className);
				}
			}
		};
		
		return handle;
	};
}