package cn.lmjia.market.wechat;


import cn.lmjia.market.core.config.MVCConfig;
import cn.lmjia.market.dealer.DealerServiceTest;
import cn.lmjia.market.wechat.config.WechatConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import me.jiangcai.wx.model.WeixinUserDetail;
import me.jiangcai.wx.test.WeixinTestConfig;
import me.jiangcai.wx.test.WeixinUserMocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;


/**
 * @author CJ
 */
@ContextConfiguration(classes = {WeixinTestConfig.class, WechatConfig.class, MVCConfig.class})
public abstract class WechatTestBase extends DealerServiceTest {

    @Autowired
    private WeixinTestConfig weixinTestConfig;

    /**
     * @return 生成一个新的微信帐号，并且应用在系统中
     */
    protected WeixinUserDetail nextCurrentWechatAccount() {
        WeixinUserDetail detail = WeixinUserMocker.randomWeixinUserDetail();
        weixinTestConfig.setNextDetail(detail);
        return detail;
    }

    @Override
    protected DefaultMockMvcBuilder buildMockMVC(DefaultMockMvcBuilder builder) {
        return super.buildMockMVC(builder);
    }

    protected MockHttpServletRequestBuilder wechatGet(String urlTemplate, Object... urlVariables) {
        return makeWechat(super.get(urlTemplate, urlVariables));
    }

    protected MockMultipartHttpServletRequestBuilder wechatFileUpload(String urlTemplate, Object... urlVariables) {
        return makeWechat(super.fileUpload(urlTemplate, urlVariables));
    }

    @SuppressWarnings("unchecked")
    private <T extends MockHttpServletRequestBuilder> T makeWechat(T builder) {
        return (T) builder.header("user-agent", "MicroMessenger");
    }

    @Override
    protected MockMvcHtmlUnitDriverBuilder buildWebDriver(MockMvcHtmlUnitDriverBuilder mockMvcHtmlUnitDriverBuilder) {
        return mockMvcHtmlUnitDriverBuilder.withDelegate(new WebConnectionHtmlUnitDriver() {
            @Override
            protected WebClient modifyWebClientInternal(WebClient webClient) {
                webClient.addRequestHeader("user-agent", "MicroMessenger");
                return super.modifyWebClientInternal(webClient);
            }
        });
    }
}
