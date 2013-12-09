<%@ tag import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ tag import="net.tanesha.recaptcha.ReCaptchaFactory" %>
 
<script type="text/javascript">var RecaptchaOptions = {theme : 'clean'};</script> 
<%
    ReCaptcha reCaptcha = ReCaptchaFactory.newReCaptcha("6Lc3WOsSAAAAAP0CCKBUIqCFmwVAeuUagYX5agLy", "6Lc3WOsSAAAAAB-d-4CKafaQ1nCzM14wkAwP79mj", false);
    out.print(reCaptcha.createRecaptchaHtml(null, null));
%>