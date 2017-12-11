<%@ page
        import="static org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("X-Frame-Options","SAMEORIGIN");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>上传文件</title>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body class="mainbody">
<!--导航栏-->
<div class="location">
    <a href="" class="home"><i></i><span>首页</span></a>
    <i class="arrow"></i> <span>客户信息录入</span>
</div>
<!--/导航栏-->
<form action="/upload?param=2" method="post" enctype="multipart/form-data">
    <div align="center"><br/>
        <fieldset style="width:50%">
            <legend>上传文件</legend><br/>
            <div class='line'>
                <div align='left' class="leftDiv">上传文件</div>
                <div align='left' class="rightDiv">
                    <input type="file" name="file" class="text">
                </div>
            </div>

            <div class='line'>
                <div align='left' class="leftDiv">上传文件说明</div>
                <div align='left' class="rightDiv"><input type="text" name="description1" class="text"></div>
            </div>

            <div class='line'>
                <div align='left' class="leftDiv"></div>
                <div align='left' class="rightDiv"><br/>
                    <input type="submit" value="  上传文件  " class="button">
                </div>
                <a href="/download?param=2">下载</a>
            </div>
        </fieldset>
    </div>
</form>
</body>
</html>