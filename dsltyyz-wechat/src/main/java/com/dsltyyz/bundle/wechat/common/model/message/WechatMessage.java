package com.dsltyyz.bundle.wechat.common.model.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * 微信消息Query
 *
 * @author: dsltyyz
 * @date: 2019/05/15
 */
@Data
@ApiModel(value = "微信消息")
public class WechatMessage {

    @ApiModelProperty(value = "消息id，64位整型")
    private Long MsgId;

    @ApiModelProperty(value = "小程序的原始ID")
    private String ToUserName;

    @ApiModelProperty(value = "发送者的openid")
    private String FromUserName;

    @ApiModelProperty(value = "消息创建时间(整型）")
    private Date CreateTime;

    @ApiModelProperty(value = "消息类型 text image miniprogrampage event")
    private String MsgType;

    @ApiModelProperty(value = "文本消息内容 text")
    private String Content;

    @ApiModelProperty(value = "图片链接 image")
    private String PicUrl;

    @ApiModelProperty(value = "图片消息媒体id image", example = "1")
    private Long MediaId;

    @ApiModelProperty(value = "标题 miniprogrampage")
    private String Title;

    @ApiModelProperty(value = "小程序appid miniprogrampage")
    private String AppId;

    @ApiModelProperty(value = "小程序页面路径 miniprogrampage")
    private String PagePath;

    @ApiModelProperty(value = "封面图片的临时cdn链接 miniprogrampage")
    private String ThumbUrl;

    @ApiModelProperty(value = "封面图片的临时素材id miniprogrampage")
    private String ThumbMediaId;

    @ApiModelProperty(value = "事件类型，user_enter_tempsession event")
    private String Event;

    @ApiModelProperty(value = " session-from 属性 event")
    private String SessionFrom;

    @ApiModelProperty(value = "状态")
    private String Status;

}
