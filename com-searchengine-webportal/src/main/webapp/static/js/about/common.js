/**
 * Created by Administrator on 2014/11/17.
 */
function goUrl(url) {
    location.href = url;
}

/**
 *
 * @param $col
 * @param items
 * @param defaultText
 */
function loadItems($col, items, defaultText, defaultValue) {
    var value = "0";
    if (!isNull(defaultValue)) {
        value = defaultValue;
    }
    $col.html("");
    if (!isNull(defaultText)) {
        $col.append("<option value='" + value + "'>" + defaultText + "</option>");
    }

    if (items == null) {
        return;
    }

    for (var i = 0; i < items.length; i++) {
        var item = items[i];
        $col.append("<option value='" + item.id + "'>" + item.name + "</option>");
    }
}

function stringIsNullOrEmpty(value) {
    if (typeof(value) == "undefined" || value == null || value == "") {
        return true;
    }

    return false;
}

function isNull(value) {
    if (typeof(value) == "undefined" || value == null) {
        return true;
    }

    return false;
}

/**
 * 设置当前排课步骤
 * @param step
 */
function setTaskStep(step) {
    $("#" + step).addClass("active");
    $("#" + step).addClass("task-" + step + "-active");
}
function setTaskRuleStep(step) {
    $("#" + step).addClass("active");
    $("#" + step).find("a").addClass("colorful");
}
//=========jquery ui

// 显示提示信息
function showMessage(msg, title) {
    $("#alert_container").remove();

    var html = "<div id=\"alert_container\"><div id=\"alert_msg\" style=\"font-size:14px;padding:5px;line-height: 22px;\"></div></div>";
    $("body").append(html);
    $("#alert_msg").html(msg);

    if (stringIsNullOrEmpty(title)) {
        title = "提示";
    }

    $("#alert_container").dialog({
        closeText: "关闭",
        title: title,
        width: 270,
        height: 120
    });
}

//===========