var IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');
var nav;

function init(rows, onClick) {
    var win = Ti.UI.createWindow({
        backgroundColor: '#FFF',
        title: 'Ti.Map',
        translucent: false
    });

    var transformedRows = [];

    for (var row in rows) {
        transformedRows.push({
            title: rows[row].title,
            hasChild: true
        });
    }

    var tableView = Ti.UI.createTableView({
        top: 0,
        data: transformedRows
    });

    tableView.addEventListener('click', function(e) {
        onClick && onClick(e);
    });

    win.add(tableView);

    if (IOS) {
        nav = Ti.UI.iOS.createNavigationWindow({
            window: win
        });
        nav.open();
    } else  {
        win.open();
    }
}

function createWindow(title) {

    var win = Ti.UI.createWindow({
        backgroundColor: '#FFF',
        title: title,
        translucent: false

    });

    if (nav) {
        nav.openWindow(win);
    } else  {
        win.open();
    }

    return win;
}

exports.init = init;
exports.createWindow = createWindow;
