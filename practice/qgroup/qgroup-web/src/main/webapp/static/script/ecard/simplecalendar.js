/**
 * @author zexin.zhaozx
 */


TB.widget.SimpleCalendar = new function() {
	
	var config_zh = {
		/* config for YUI Calendar Container */
		'DATE_FIELD_DELIMITER': '-',
		'DATE_RANGE_DELIMITER': '~',
		'MDY_YEAR_POSITION': 1,
		'MDY_MONTH_POSITION': 2,
		'MDY_DAY_POSITION': 3,
		'MY_YEAR_POSITION': 1,
		'MY_MONTH_POSITION': 2,
		'MONTHS_SHORT': ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'], 
		'MONTHS_LONG': ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
		'WEEKDAYS_1CHAR': ['日','一', '二', '三', '四', '五', '六'],
		'WEEKDAYS_SHORT': ['日','一', '二', '三', '四', '五', '六'], 
		'WEEKDAYS_MEDIUM': ['日','一', '二', '三', '四', '五', '六'],
		'WEEKDAYS_LONG': ['日','一', '二', '三', '四', '五', '六'],
		'MY_LABEL_YEAR_POSITION': 1,
		'MY_LABEL_MONTH_POSITION': 2,
		'MY_LABEL_YEAR_SUFFIX': '年',
		'MY_LABEL_MONTH_SUFFIX': '月',
		'LOCALE_MONTHS': 'short' 
	}
	
	var navConfig_zh = {   
		strings: {   
			month: '选择月份',   
			year: '输入年份',   
			submit: '确定',   
			cancel: '取消',   
			invalidYear: '请输入有效的年份'  
		},   
		initialFocus: 'year'  
	};  


	var enhanceConfig = {
		/* for zh */
		"YEAR_MAX": 2020,
		"YEAR_MIN": 1970		
	}
	
	var _leadingZero = function(n) {
		return ((n < 10) ? "0" : "") + n;
	}
	
	var _toggleCal = function() {
		this[this._status == 'show'?'hide':'show']();
	}
	
	var _createContainer = function(id, root) {
		var container = document.createElement('div');
		container.id = id + '_container';
		return ($(root) || document.body).appendChild(container);
	}
	
	/**
	 * 根据字符串得到Date对象
	 */ 
	var _parsePageDate = function() {
		var p = /^(\d{4})-(?:[0]?)(\d{1,2})-(?:[0]?)(\d{1,2})$/;
		return function(str) {
			try {
				var m = str.match(p).slice(1);
				return new Date(m[0], m[1]-1, m[2]);
			} catch(e) {
				return str;
			}
		}
	}();

	/**
	 * date 可选值可以是字符串: yyyy-mm-dd, today  或者 [y,m,d]， 不做校验
	 * @param {Object} date
	 */
	var _parseDate = function(date) {
		if (YAHOO.lang.isString(date)) {		
			if (date.indexOf('-') != -1) return date;
			if ('today' == date.toLowerCase()) {
				var today = new Date();
				return _fmtDate([today.getFullYear(), today.getMonth()+1, today.getDate()]);
			}
		} else if (YAHOO.lang.isArray(date)) {
			return _fmtDate(date);
		}
	}
	
	var _fmtDate = function(arr) {
		return arr[0] + "-" + _leadingZero(arr[1]) + "-" + _leadingZero(arr[2]);
	}
	
	return {
		/* 中文配置参数 */
		config_zh: config_zh,
		
		/* 修复YUI Calendar 对象，增加部分功能 */
		enhance: function(cal, config) {
			
			config = TB.applyIf(config||{}, enhanceConfig);

			if (config.maxdate) { //maxdate 只接受字符创形式
				config.maxdate = _parseDate(config.maxdate);
				cal.cfg.setProperty('pagedate', _parsePageDate(config.maxdate));
				cal.cfg.setProperty('maxdate', config.maxdate);
			}
			if (config.mindate) { //mindate 只接受字符创形式
				config.mindate = _parseDate(config.mindate);
				cal.cfg.setProperty('pagedate', _parsePageDate(config.mindate));
				cal.cfg.setProperty('mindate', config.mindate);
			} 
			if (config.selected) { //selected 只接受字符创形式
				config.selected = _parseDate(config.selected);
				cal.cfg.setProperty('pagedate', _parsePageDate(config.selected));
				cal.cfg.setProperty('selected', config.selected);
			}
			
			/* 非本月的日子也可选择 */
			if (config.enableOOM) {
				cal.renderCellNotThisMonth = function(workingDate, cell) {
					YAHOO.util.Dom.addClass(cell, this.Style.CSS_CELL_OOM);
					cell.innerHTML=workingDate.getDate();
					return cal.renderCellDefault(workingDate, cell); //return YAHOO.widget.Calendar.STOP_RENDER;
				};			
			}
			
			if (!config.pages && config.enableSelectYear) {
				cal.buildMonthLabel = function() {
					var pageDate = this.cfg.getProperty(YAHOO.widget.Calendar._DEFAULT_CONFIG.PAGEDATE.key);
					var cy = pageDate.getFullYear(), cm = pageDate.getMonth();
					var ySel = ['<select class="calyearselector">'];
					for (var y=config.YEAR_MIN; y <= config.YEAR_MAX; ++y) {
						ySel.push('<option value="' + y + '"' + (cy==y?' selected="selected"':'') + '>' + y + '</option>');
					}
					ySel.push('</select>')
					var mSel = ['<select class="calmonthselector">'];
					for (var m=0; m < 12; ++m) {
						mSel.push('<option value="' + m + '"' + (cm==m?' selected="selected"':'') + '>' + (m+1) + '</option>');
					}
					mSel.push('</select>')
					return ySel.join('') + this.Locale.MY_LABEL_YEAR_SUFFIX + mSel.join('') + this.Locale.MY_LABEL_MONTH_SUFFIX;
				}
				cal.renderEvent.subscribe(function(){
					var pd = this.cfg.getProperty(YAHOO.widget.Calendar._DEFAULT_CONFIG.PAGEDATE.key);
					//超出时间范围时隐藏前后导航
					var pdy = pd.getFullYear(), pdm = pd.getMonth();
					if (pdy == config.YEAR_MAX && pdm == 11 && this.linkRight ) {
						$D.setStyle(this.linkRight, 'display', 'none');					
					} else if (pdy == config.YEAR_MIN && pdm == 0 && this.linkLeft) {
						$D.setStyle(this.linkLeft, 'display', 'none');
					}
					var sels = this.oDomContainer.getElementsByTagName('select');
					$E.on(sels[0], 'change', function(ev, cal) {
						cal.setYear(this.value);
						cal.render();
					}, this);
					$E.on(sels[1], 'change', function(ev, cal) {
						cal.setMonth(this.value);
						cal.render();
					}, this);
				}, cal, true);
			} 
		},
	
		init : function(input, container, toggle, config) {
			container = $(container);
			input = $(input);
			toggle = $(toggle) || input;
			config = config || {};
			
			var calId = $D.generateId(null, '_tbpc_');
			//如果container id为空，创建一个，并append到 config.containerRoot 元素下
			if (!container) {
				container = _createContainer(calId, config.containerRoot);
			}		
			
			var cal;
			
			//中文年份导航
			if (config.navigator) {
				if(YAHOO.lang.isObject(config.navigator))
					TB.applyIf(config.navigator, navConfig_zh);
				else
					config.navigator = navConfig_zh;
			}
			
			if(config.pages && config.pages > 1) {
				cal = new YAHOO.widget.CalendarGroup(calId, container.id, TB.apply(config_zh, config));	
			} else {
				cal = new YAHOO.widget.Calendar(calId, container.id, TB.apply(config_zh, config));
			}
			//fix yui bug
			this.enhance(cal, config);		
			
			if (YAHOO.lang.isFunction(config.onSelect)) {
				cal.selectEvent.subscribe(config.onSelect, cal, true);
			}
			if (YAHOO.lang.isFunction(config.onBeforeSelect)) {
				cal.beforeSelectEvent.subscribe(config.onBeforeSelect, cal, true);
			}
			if (YAHOO.lang.isFunction(config.onClear)) {
				cal.clearEvent.subscribe(config.onClear, cal, true);
			}
			if (config.footer) {
				cal.renderFooter = function(html) {
					html.push('<tfoot><tr><td colspan="8">' + config.footer + '</td></tr></tfoot>');
					return html;
				}
			}		
			
			var selectHandler = function(type,args,obj) {
				var selected = args[0];
				input.value = _fmtDate(selected[0]);
				handle.hide();			
			}
	
			$D.setStyle(container, 'position', 'absolute');		
			cal.selectEvent.subscribe(selectHandler, cal, true);
	
			cal.hide();
			cal.render();
			
			var handle = {};
			handle._status = 'hide';
			handle._beforeShowEvent = new YAHOO.util.CustomEvent('beforeShow', handle, false, YAHOO.util.CustomEvent.FLAT);
			handle.calObj = cal;
			
			handle.hide = function() {
				cal.hide();
				handle._status = 'hide';
			}
			handle.show = function() {
				handle._beforeShowEvent.fire();
				cal.show();
				handle._status = 'show';
				var inputPos = $D.getXY(input);
				inputPos[1] += input.offsetHeight;
				$D.setXY(container, inputPos);
			}
			handle.select = function(date) {
				return cal.select(date);
			}
			handle.getSelectedDates = function() {
				return cal.getSelectedDates();
			}
			
			$E.on(toggle, (input===toggle)?'focus':'click', _toggleCal, handle, true);
			$E.on(document, 'mousedown', function(ev) {
				var target = $E.getTarget(ev);
				if (!(target === input || target === toggle || $D.isAncestor(container, target)))
					handle.hide();
			});	
			$E.on(document, 'keydown', function(ev) {
				if (ev.keyCode == 27)
					handle.hide();
			});	
				
			return handle;
		},
		
		/**
		 * 初始化一对彼此约束的日历输入框
		 * @param {Array} inputs
		 * @param {Array} containers  or null
		 * @param {Array} toggles
		 * @param {Object} config
		 */
		initRange: function(inputs, containers, toggles, config) {
			if (!YAHOO.lang.isArray(inputs) || inputs.length < 2) {
				alert('You need pass an array including tow input fields.');
				return;
			}
			var cal0Handle = this.init(inputs[0], containers?containers[0]:null, toggles?toogles[0]:null, config);
			var cal1Handle = this.init(inputs[1], containers?containers[1]:null, toggles?toogles[1]:null, config);
			
			var _resetRange = function() {
				var partnerCal = arguments[1][0];
				var idx = arguments[1][1];
				partnerCal.hide();
				var dateVal = partnerCal._input.value;
				if (dateVal) {
					//this.calObj.cfg.setProperty('pagedate', _parsePageDate(dateVal));					
					this.calObj.cfg.setProperty(idx==1?'mindate':'maxdate', dateVal);
					this.calObj.render();					
				}
			} 
			
			cal0Handle._beforeShowEvent.subscribe(_resetRange, [cal1Handle, 0], cal0Handle);
			cal0Handle._input = inputs[0];
			cal1Handle._beforeShowEvent.subscribe(_resetRange, [cal0Handle, 1], cal1Handle);
			cal1Handle._input = inputs[1];
			
			return [cal0Handle, cal1Handle];
		}	
	}	
}	