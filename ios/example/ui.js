
function init(rows, onClick) {
	Ti.UI.backgroundColor = '#FFF';
    var win = Ti.UI.createWindow({
        backgroundColor: '#FFF',
        top: top
    });
    
    var tableView = Ti.UI.createTableView({
        top: 0,
        data: rows
    });
    tableView.addEventListener('click', function(e){
        onClick && onClick(e);
    });
    
    win.add(tableView);
    
    win.open();
}

function createWindow() {
    var win  = Ti.UI.createWindow({
        backgroundColor: '#FFF',
        fullscreen: false,
        top: top
    });
    
    var backButton = Ti.UI.createLabel({
        top: 0, width: '100%', height: '10%',
        backgroundColor: '#000',
        color: '#FFF',
        text: 'Back',
        textAlign: 'center'
    });
    
    backButton.addEventListener('click', function() {
        win.close();
    });
    
    win.add(backButton);
    
    return win;
}

exports.init = init;
exports.createWindow = createWindow;
