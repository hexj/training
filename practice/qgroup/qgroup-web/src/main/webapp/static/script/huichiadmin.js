$(document).ready(function(){
//    alert("www");
    $(".deleteItem").on("click",function(event){
        var current = event.target;

        $.ajax({
                type : "post",
                url :window.g_config.deleteItemUrl,
                data : {
                    id :$(event.target).next().val(),
                    itemId : $(event.target).attr("data-itemId")
                },
                success : function(msg) {
                    alert(msg);
                    location.reload();
                },
                error : function(data) {
                    alert("error")
                }
        }
        );
    });


    $(".addItem").on("click",function(event){
        $.ajax({
                type : "post",
                url :window.g_config.addItemUrl,
                data : {
                    moduleId : $("input[name='moduleId']").val(),
                    appId :  $(event.target).next().val()
                },
                success : function(data) {
                    alert(data);
                    location.reload();
                },
                error : function(data) {
                    alert("error")
                }
            }
        );
    });

    $(".periodOnLine").on("click",function(event){
        if(confirm("确认提交么")) {
//        event.preventDefault();
            var date1 = new Date($('.timeTrigger1').val());
            var date2 = new Date($('.timeTrigger2').val());
            if(!date1 || !date2) {
                alert("日期不能为空");
                return;
            }
            if(date1 >= date2) {
                alert("开始时间不能在结束时间之后");
                return;
            }


            $.ajax({
                    type : "post",
                    url : window.g_config.periodOnLineUrl,
                    data : {
                        periodId : $(event.target).attr("data-period")
                    },
                    success : function(data) {
                        alert(data);
                        location.reload();
                    },
                    error : function(data) {
                        alert("error:"+data)
                    }
                }
            );
        }

    });


    $('.J_DelModule').on('click', function(e){
        e.preventDefault();
        var id = $(this).attr('data-id');
        $.ajax({
            url: '/huichiadmin/json/deleteModule.do',
            data: {moduleId: id},
            type: 'POST',
            success: function(d){
                d = JSON.parse(d);
                if(d.success == 'true'){
                    location.reload();
                }
            }
        })
    });


    $('.J_ItemSort').on('blur', function(){
        var self = this,
            sort = $(this).val(),
            id = $(this).attr('data-id');
        $.ajax({
            url: '/huichiadmin/json/sortItem.do',
            data: {sort:sort, id:id},
            type: 'POST',
            success: function(d){
                if(d.success == 'true'){
                    $(self).css({'background':'#9c0'});
                }
            }
        })
    });


    $('.J_ModuleSort').on('blur', function(){
        var self = this,
            sort = $(this).val(),
            id = $(this).attr('data-id');
        $.ajax({
            url: '/huichiadmin/json/sortModule.do',
            data: {sort:sort, id:id},
            type: 'POST',
            success: function(d){
                d = JSON.parse(d);
                if(d.success == 'true'){
                    $(self).css({'background':'#9c0'});
                }
            }
        });
    });

    $('.J_ClearCache').on('click', function(){
        var self = this,
//            sort = $(this).val(),
            id = $(this).attr('data-id');
        $.ajax({
            url: '/huichiadmin/json/clearCache.do',
            data: {id:id},
            dataType:"json",
            type: 'POST',
            success: function(d){
//                d = JSON.parse(d);
                if(d.success == 'true'){
                    alert("成功");
                                    }
            }
        });
    });

});

function submitPeriod() {

}

/**
 * 挑食商品限量库存订正
 * @param moduleId
 * @param itemId
 * @param syncdb 是否更新无线端库存
 */
function setTiaoshiItemQuantity(moduleId, itemId, syncdb){
	
	if(!moduleId || !itemId) {
		alert("参数错误");
		return false;
	}
	
	var param = {
		operate: "setTiaoshiQuantity",
		item_id: itemId,
		
	};
	
	if(syncdb && moduleId ){
		param.module_id = moduleId;
	}
	var quantity = $('#itemQuentity_' + moduleId + '_' + itemId).val();
	if(quantity && quantity < 0){
		alert("库存数必须大于等于0");
		return false;
	}
	param.quantity = quantity;
	$.ajax({
        url: "/adminController.do",
        data: param,
        dataType:"json",
        type: 'POST',
        success: function(d){
            if(d.success){
                alert("成功");
            }else if(JSON && JSON.stringify){
            	alert("失败：" + JSON.stringify(d));
            }else {
            	alert("失败:" + d.msg);
            }
        }
    });
}
