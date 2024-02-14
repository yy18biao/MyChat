// 两个标签之间的转换
function initSwitchTab() {
    let tabSession = document.querySelector('.tab .tab-session');
    let tabFriend = document.querySelector('.tab .tab-friend');
    let lists = document.querySelectorAll('.list');

    tabSession.onclick = function () {
        // 设置图标
        tabSession.style.backgroundImage = 'url(img/session1.png)';
        tabFriend.style.backgroundImage = 'url(img/friend2.png)';
        // 让会话列表显示出来, 让好友列表进行隐藏
        lists[0].classList = 'list';
        lists[1].classList = 'list hide';
    }

    tabFriend.onclick = function () {
        // 设置图标
        tabSession.style.backgroundImage = 'url(img/session2.png)';
        tabFriend.style.backgroundImage = 'url(img/friend1.png)'
        // 让好友列表显示, 让会话列表隐藏
        lists[0].classList = 'list hide';
        lists[1].classList = 'list';
    }
}
initSwitchTab();

// 获取当前用户名
function getUserName() {
    jQuery.ajax({
        url: "/user/getusername",
        type: "POST",
        data: {},
        success: function (res) {
            if (res.id && res.id > 0) {
                var userDiv = document.querySelector('.main .left .user');
                userDiv.innerHTML = res.username;
                userDiv.setAttribute("user-id", res.id);
            } else {
                alert("当前用户未登录!");
                location.href = "login.html";
            }
        }
    });
}
getUserName();

// 获取好友列表
function getFriends() {
    jQuery.ajax({
        url: "/user/friends",
        type: "POST",
        success: function (res) {
            // 先把之前的好友列表的内容清空
            let friendListUL = document.querySelector('#friend-list');
            friendListUL.innerHTML = '';
            if (res != null) {
                for (let friend of res) {
                    let li = document.createElement('li');
                    li.innerHTML = '<h4>' + friend.friendName + '</h4>';
                    li.setAttribute('friend-id', friend.friendId);
                    friendListUL.appendChild(li);

                    // 添加点击事件
                    li.onclick = function () {
                        // 参数表示区分了当前用户点击的是哪个好友. 
                        clickFriend(friend);
                    }
                }
            } else {
                console.log('获取好友列表失败!');
            }
        }
    })
}
getFriends();

// 获取所有消息会话
function getSessions() {
    jQuery.ajax({
        url: "/session/getsessions",
        type: "POST",
        success: function (res) {
            // 清空之前的会话列表
            let sessionListUL = document.querySelector('#session-list');
            sessionListUL.innerHTML = '';
            // 遍历响应的数组, 针对结果来构造页面
            for (let session of res) {
                // 针对 lastMessage 的长度进行截断处理
                if (session.lastMessage.length > 10) {
                    session.lastMessage = session.lastMessage.substring(0, 10) + '...';
                }

                let li = document.createElement('li');
                // 把会话 id 保存到 li 标签的自定义属性中. 
                li.setAttribute('message-session-id', session.sessionId);
                li.innerHTML = '<h3>' + session.friends[0].friendName + '</h3>'
                    + '<p>' + session.lastMessage + '</p>';
                sessionListUL.appendChild(li);

                // 给 li 标签新增点击事件
                li.onclick = function () {
                    // 这个写法, 就能保证, 点击哪个 li 标签
                    // 此处对应的 clickSession 函数的参数就能拿到哪个 li 标签. 
                    clickSession(li);
                }
            }
        }
    });
}
getSessions();

function clickSession(currentLi) {
    // 设置高亮
    let allLis = document.querySelectorAll('#session-list>li');
    activeSession(allLis, currentLi);
    // 获取指定会话的历史消息 TODO
    let sessionId = currentLi.getAttribute("message-session-id");
    getHistoryMessage(sessionId);
}

function activeSession(allLis, currentLi) {
    // 这里的循环遍历, 更主要的目的是取消未被选中的 li 标签的 className
    for (let li of allLis) {
        if (li == currentLi) {
            li.className = 'selected';
        } else {
            li.className = '';
        }
    }
}

// 获取会话的历史消息
function getHistoryMessage(sessionId) {
    console.log("获取历史消息 sessionId=" + sessionId);
    // 先清空右侧列表中的已有内容
    let titleDiv = document.querySelector('.right .title');
    titleDiv.innerHTML = '';
    let messageShowDiv = document.querySelector('.right .message-show');
    messageShowDiv.innerHTML = '';

    // 先找到当前选中的会话是哪个. 被选中的会话带有 selected 类的. 
    let selectedH3 = document.querySelector('#session-list .selected>h3');
    if (selectedH3) {
        // selectedH3 可能不存在的. 比如页面加载阶段, 可能并没有哪个会话被选中. 
        // 也就没有会话带有 selected 标签. 此时就无法查询出这个 selectedH3
        titleDiv.innerHTML = selectedH3.innerHTML;
    }

    if (sessionId != null) {
        // 获取到该会话的历史消息. 
        jQuery.ajax({
            type: 'POST',
            url: '/message/getallmessage?sessionId=' + sessionId,
            success: function (res) {
                // 直接遍历即可. 
                for (let message of res) {
                    addMessage(messageShowDiv, message);
                }
                // 在构造好消息列表之后, 控制滚动条自动滚动到最下方. 
                scrollBottom(messageShowDiv);
            }
        });
    }
}

// 拼接会话中的消息
function addMessage(messageShowDiv, message) {
    // 使用这个 div 表示一条消息
    let messageDiv = document.createElement('div');
    // 此处需要针对当前消息是不是用户自己发的, 决定是靠左还是靠右. 
    let selfUsername = document.querySelector('.left .user').innerHTML;
    if (selfUsername == message.fromName) {
        // 消息是自己发的. 靠右
        messageDiv.className = 'message message-right';
    } else {
        // 消息是别人发的. 靠左
        messageDiv.className = 'message message-left';
    }
    messageDiv.innerHTML = '<div class="box">'
        + '<h4>' + message.fromName + '</h4>'
        + '<p>' + message.content + '</p>'
        + '</div>';
    messageShowDiv.appendChild(messageDiv);
}

// 把 messageShowDiv 里的内容滚动到底部. 
function scrollBottom(elem) {
    // 获取到可视区域的高度
    let clientHeight = elem.offsetHeight;
    // 获取到内容的总高度
    let scrollHeight = elem.scrollHeight;
    // 进行滚动操作, 第一个参数是水平方向滚动的尺寸. 第二个参数是垂直方向滚动的尺寸
    elem.scrollTo(0, scrollHeight - clientHeight);
}

// 点击好友列表项, 触发的函数
function clickFriend(friend) {
    // 先判定一下当前这个好友是否有对应的会话. 
    let sessionLi = findSessionByName(friend.friendName);
    let sessionListUL = document.querySelector('#session-list');
    if (sessionLi) {
        // 如果存在匹配的结果, 就把这个会话设置成选中状态, 获取历史消息, 并且置顶. 
        sessionListUL.insertBefore(sessionLi, sessionListUL.children[0]);
        // 此处设置会话选中状态, 获取历史消息, 这俩功能其实在上面的 clickSession 中已经有了. 
        sessionLi.click();
    } else {
        // 如果不存在匹配的结果, 就创建个新会话(创建 li 标签 + 通知服务器)
        sessionLi = document.createElement('li');
        // 构造 li 标签内容. 由于新会话没有 "最后一条消息", p 标签内容就设为空即可
        sessionLi.innerHTML = '<h3>' + friend.friendName + '</h3>' + '<p></p>';
        // 把标签进行置顶
        sessionListUL.insertBefore(sessionLi, sessionListUL.children[0]);
        sessionLi.onclick = function () {
            clickSession(sessionLi);
        }
        sessionLi.click();
        // 发送消息给服务器, 告诉服务器当前新创建的会话是啥样的. 
        createSession(friend.friendId, sessionLi);
    }
    // 还需要把标签页给切换到 会话列表. 
    let tabSession = document.querySelector('.tab .tab-session');
    tabSession.click();
}

function findSessionByName(username) {
    // 先获取到会话列表中所有的 li 标签
    // 然后依次遍历, 看看这些 li 标签谁的名字和要查找的名字一致. 
    let sessionLis = document.querySelectorAll('#session-list>li');
    for (let sessionLi of sessionLis) {
        // 获取到该 li 标签里的 h3 标签, 进一步得到名字
        let h3 = sessionLi.querySelector('h3');
        if (h3.innerHTML == username) {
            return sessionLi;
        }
    }
    return null;
}

// friendId 是构造 HTTP 请求时必备的信息
function createSession(friendId, sessionLi) {
    jQuery.ajax({
        type: 'post',
        url: '/session/createsession?friendId=' + friendId,
        success: function (res) {
            console.log("会话创建成功! sessionId = " + res);
            sessionLi.setAttribute('message-session-id', res);
        },
        error: function () {
            console.log('会话创建失败!');
        }
    });
}

// 创建websocket实例
let websocket = new WebSocket("ws://" + location.host + "/webSocket");

// 连接成功回调
websocket.onopen = function () {
    console.log("websocket 连接成功!");
}

// 收到消息回调
websocket.onmessage = function (e) {
    console.log("websocket 收到消息! " + e.data);
    // 此时收到的 e.data 是个 json 字符串, 需要转成 js 对象
    let resp = JSON.parse(e.data);
    if (resp.type == 'message') {
        // 处理消息响应
        handleMessage(resp);
    } else {
        console.log("resp.type 不符合要求!");
    }
}

// 关闭连接回调
websocket.onclose = function () {
    console.log("websocket 连接关闭!");
}

// 异常回调
websocket.onerror = function () {
    console.log("websocket 连接异常!");
}

// 发送消息的按钮设计
function initSendButton() {
    // 获取到发送按钮 和 消息输入框
    let sendButton = document.querySelector('.right .ctrl button');
    let messageInput = document.querySelector('.right .message-input');
    // 给发送按钮注册一个点击事件
    sendButton.onclick = function () {
        // 先针对输入框的内容做个简单判定 比如输入框内容为空, 则啥都不干
        if (!messageInput.value) {
            // value 的值是 null 或者 '' 都会触发这个条件
            return;
        }
        // 获取当前选中的 li 标签的 sessionId
        let selectedLi = document.querySelector('#session-list .selected');
        if (selectedLi == null) {
            // 当前没有 li 标签被选中. 
            return;
        }
        let sessionId = selectedLi.getAttribute('message-session-id');
        // 构造 json 数据
        let req = {
            type: 'message',
            sessionId: sessionId,
            content: messageInput.value
        };
        req = JSON.stringify(req);
        console.log("[websocket] send: " + req);
        // 通过 websocket 发送消息
        websocket.send(req);
        // 发送完成之后, 清空之前的输入框
        messageInput.value = '';
    }
}
initSendButton();

// 接收到消息后的处理
function handleMessage(resp) {
    // 根据响应中的 sessionId 获取到当前会话对应的 li 标签. 
    // 如果 li 标签不存在, 则创建一个新的
    let curSessionLi = findSessionLi(resp.sessionId);
    if (curSessionLi == null) {
        curSessionLi = document.createElement('li');
        curSessionLi.setAttribute('message-session-id', resp.sessionId);
        // 此处 p 标签内部应该放消息的预览内容. 一会后面统一完成, 这里先置空
        curSessionLi.innerHTML = '<h3>' + resp.fromName + '</h3>'
            + '<p></p>';
        // 给这个 li 标签也加上点击事件的处理
        curSessionLi.onclick = function () {
            clickSession(curSessionLi);
        }
    }

    // 把新的消息, 显示到会话的预览区域 (li 标签里的 p 标签中)
    //    如果消息太长, 就需要进行截断. 
    let p = curSessionLi.querySelector('p');
    p.innerHTML = resp.content;
    if (p.innerHTML.length > 10) {
        p.innerHTML = p.innerHTML.substring(0, 10) + '...';
    }

    // 把收到消息的会话, 给放到会话列表最上面. 
    let sessionListUL = document.querySelector('#session-list');
    sessionListUL.insertBefore(curSessionLi, sessionListUL.children[0]);

    // 如果当前收到消息的会话处于被选中状态, 则把当前的消息给放到右侧消息列表中. 
    // 新增消息的同时, 注意调整滚动条的位置, 保证新消息虽然在底部, 但是能够被用户直接看到. 
    if (curSessionLi.className == 'selected') {
        // 把消息列表添加一个新消息. 
        let messageShowDiv = document.querySelector('.right .message-show');
        addMessage(messageShowDiv, resp);
        scrollBottom(messageShowDiv);
    }
}

// 获取到所有的会话列表中的 li 标签
function findSessionLi(targetSessionId) {
    let sessionLis = document.querySelectorAll('#session-list li');
    for (let li of sessionLis) {
        let sessionId = li.getAttribute('message-session-id');
        if (sessionId == targetSessionId) {
            return li;
        }
    }
    // 啥时候会触发这个操作, 就比如如果当前新的用户直接给当前用户发送消息, 此时没存在现成的 li 标签
    return null;
}
