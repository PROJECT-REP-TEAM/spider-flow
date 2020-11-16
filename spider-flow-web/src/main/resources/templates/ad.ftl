<#assign class1="HqewActiveViewElement">
<#assign class2="HqewCreativeContainerClass">
<#assign pid1="DfaVisibilityIdentifier_${adCode}">
<#assign ahrefId="img_hqew_${adCode}">

document.write('\x3cdiv class="${class1}" ' + 'id="${pid1}"\x3e');
document.write('\x3cdiv class="${class2}" ' + 'id="${ahrefId}"\x3e');
(function () {
  var m = document.createElement('meta');
  m.setAttribute('data-jc', '${dataJc}');
  m.setAttribute('data-jc-version', '${dataJcVersion}');
  var ss = document.getElementsByTagName('script')[0];
  if (ss && ss.parentNode) {
    ss.parentNode.insertBefore(m, ss);
  }
})();
document.write('\x3ca target\x3d\x22_blank\x22 id\x3d\x22${ahrefId}\x22 href\x3d\x22${clickUrl}\x22\x3e\x3cimg src\x3d\x22${imgUrl}\x22 alt\x3d\x22Advertisement\x22 border\x3d\x220\x22 width\x3d\x22${imgWidth}\x22 height\x3d\x22${imgHeight}\x22 style\x3d\x22display:block\x22\x3e\x3c/a\x3e');
document.write('\x3cscript data-jc\x3d\x22${dataJc}\x22 data-jc-version\x3d\x22${dataJcVersion}\x22 data-jcp-a-id\x3d\x22${ahrefId}\x22\x3e(function(){document.getElementById("${ahrefId}").addEventListener(\x22mousedown\x22,function(c){var h\x3dc.currentTarget,a\x3dh.querySelector(\x22img[alt]\x22);if(a){var d\x3dh.href;var p\x3d+Math.round(c.clientX-a.offsetLeft);c\x3d+Math.round(c.clientY-a.offsetTop);a\x3d+a.width+\x22x\x22+ +a.height;var k\x3d/^(https?:[^:?]+[/]pcs[/]click[^/]+?)(?:\x26nx[^\x26]+\x26ny[^\x26]+\x26dim[^\x26]+)?(\x26adurl\x3d.*)/.exec(d);d\x3dk?k[1]+(\x22\x26nx\x3d\x22+p+\x22\x26ny\x3d\x22+c+\x22\x26dim\x3d\x22+a)+k[2]:d;(h.href\x3dd)}});}).call(this);\x3c/script\x3e');
document.write('\x3c/div\x3e');
document.write('\x3c/div\x3e');

(function () {

  function S(a, b, c) {
    a.hqew_image_requests || (a.hqew_image_requests = []);
    var d = a.document.createElement("img");
    c && (d.referrerPolicy = "no-referrer");
    d.src = b;
    a.hqew_image_requests.push(d)
  }

  function btrp(c) {
    m = window;
    if (d = m.navigator) {
      d = m.navigator.userAgent,
        d = /Chrome/.test(d) && !/Edge/.test(d) ? !0 : !1;
      d && m.navigator.sendBeacon ? m.navigator.sendBeacon(c) : S(m, c, !1)
    } else S(m, c, !1)
  }
  window.stcc = btrp("${viewUrl}",
    document.getElementById("${adCode}"));
})();
