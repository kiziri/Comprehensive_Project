console.log("js result");


function prettyJson(jsonText) {
    if (!jsonText) {
        return jsonText;
    }
    var prettyJson = new Array();
    var depth = 0;
    var currChar;
    var prevChar;
    var doubleQuoteIn = false;
    for (var i = 0; i < jsonText.length; i++) {
        currChar = jsonText.charAt(i);
        if (currChar == '\"') {
            if (prevChar != '\\') {
                doubleQuoteIn = !doubleQuoteIn;
            }
        }
        switch (currChar) {
            case '{':
                prettyJson.push(currChar);
                if (!doubleQuoteIn) {
                    prettyJson.push('\n');
                    insertTab(prettyJson, ++depth);
                }
                break;
            case '}':
                if (!doubleQuoteIn) {
                    prettyJson.push('\n');
                    insertTab(prettyJson, --depth);
                }
                prettyJson.push(currChar);
                break;
            case ',':
                prettyJson.push(currChar);
                if (!doubleQuoteIn) {
                    prettyJson.push('\n');
                    insertTab(prettyJson, depth);
                }
                break;
            default:
                prettyJson.push(currChar);
                break;
        }
        prevChar = currChar;
    }
    return prettyJson.join('');
}


function insertTab(prettyJson, depth) {
    for (var i = 0; i < depth; i++) {
        prettyJson.push(TAB);
    }
}





