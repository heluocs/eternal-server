package xyz.goome.eternal.loginserver.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.goome.eternal.common.constants.Config;
import xyz.goome.eternal.common.constants.ErrorCode;
import xyz.goome.eternal.common.entity.Account;
import xyz.goome.eternal.common.entity.Result;
import xyz.goome.eternal.common.message.AccountMessage;
import xyz.goome.eternal.common.message.Message;
import xyz.goome.eternal.common.service.AccountService;
import xyz.goome.eternal.common.utils.ByteUtil;
import xyz.goome.eternal.loginserver.rpc.ThriftClient;
import xyz.goome.eternal.loginserver.utils.SpringContextUtil;

/**
 * Created by matrix on 16/3/29.
 */
@Component
public class AccountModule {

    private Logger logger = LoggerFactory.getLogger(AccountModule.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static AccountModule instance = new AccountModule();

    private AccountModule() {

    }

    public void dispatchAction(Channel channel, int action, byte[] msgbody) {
        switch (action) {
            case Message.MSG_ACCOUNT_LOGIN_REQUEST_C2S & 0xffff:
                onAccountLoginRequest(channel, msgbody);
                break;
            case Message.MSG_ACCOUNT_REGIST_REQUEST_C2S & 0xffff:
                onAccountRegistRequest(channel, msgbody);
                break;
            default:
                break;
        }
    }

    public static AccountModule getInstance() {
        return instance;
    }

    /**
     * 客户端登录请求
     * @param channel
     * @param msgbody
     */
    private void onAccountLoginRequest(Channel channel, byte[] msgbody) {
        AccountMessage.MsgAccountLoginRequest request = null;
        try {
            request = AccountMessage.MsgAccountLoginRequest.parseFrom(msgbody);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        String account = request.getAccount();
        String password = request.getPassword();

        logger.info("account:{} password:{}", account, password);

        ThriftClient thriftClient = (ThriftClient) SpringContextUtil.getBean("thriftClient");
        AccountService.Client accountService = (AccountService.Client)thriftClient.getClient("AccountService");
        String jsonResult = null;
        try {
            jsonResult = accountService.accountLogin(account, password);
        } catch (TException e) {
            e.printStackTrace();
        }
        System.out.println(jsonResult);
        JsonObject jsonObject = new JsonParser().parse(jsonResult).getAsJsonObject();

        boolean success = jsonObject.get("success").getAsBoolean();
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        String authid = data.get("authid").getAsString();
        int currServId = data.get("currServId").getAsInt();

        AccountMessage.MsgAccountLoginResponse.Builder builder = AccountMessage.MsgAccountLoginResponse.newBuilder();
        builder.setAuthid(authid);
        builder.setCurrServId(currServId);
        builder.setRetcode(ErrorCode.COMMON_SUCCESS);
        builder.setSuccess(true);

        /*
        AccountMessage.MsgGateServer.Builder gateserver = AccountMessage.MsgGateServer.newBuilder();
        gateserver.setName("青龙");
        gateserver.setIp("127.0.0.1");
        gateserver.setPort(8888);
        builder.addServers(gateserver);
        */

        byte[] respbody = builder.build().toByteArray();
        byte[] msgno = ByteUtil.int2byte(Message.MSG_ACCOUNT_LOGIN_RESPONSE_S2C);
        short msglen = (short) (Config.MSG_LENGTH + msgno.length + respbody.length);

        ByteBuf byteBuf = Unpooled.buffer(msglen);
        byteBuf.writeBytes(ByteUtil.short2byte(msglen));
        byteBuf.writeBytes(msgno);
        byteBuf.writeBytes(respbody);

        doAccountLoginResponse(channel, byteBuf);
    }

    /**
     * 登录成功后，返回认证信息和服务器列表
     * @param channel
     * @param byteBuf
     */
    private void doAccountLoginResponse(Channel channel, ByteBuf byteBuf) {
        //TODO return server list to client
        channel.writeAndFlush(byteBuf);
    }

    /**
     * 客户端注册请求
     * @param channel
     * @param msgbody
     */
    private void onAccountRegistRequest(Channel channel, byte[] msgbody) {
        AccountMessage.MsgAccountRegistRequest request = null;
        try {
            request = AccountMessage.MsgAccountRegistRequest.parseFrom(msgbody);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        String account = request.getAccount();
        String password = request.getPassword();

        ThriftClient thriftClient = (ThriftClient) SpringContextUtil.getBean("thriftClient");
        AccountService.Client accountService = (AccountService.Client)thriftClient.getClient("AccountService");
        String jsonResult = null;
        try {
            jsonResult = accountService.accountRegist(account, password);
        } catch (TException e) {
            e.printStackTrace();
        }
        System.out.println(jsonResult);
        Result result = gson.fromJson(jsonResult, Result.class);
        String authid = result.getData().toString();

        AccountMessage.MsgAccountRegistResponse.Builder builder = AccountMessage.MsgAccountRegistResponse.newBuilder();
        builder.setAuthid(authid);
        builder.setCurrServId(0);
        builder.setRetcode(ErrorCode.COMMON_SUCCESS);
        builder.setSuccess(true);

        byte[] respbody = builder.build().toByteArray();
        byte[] msgno = ByteUtil.int2byte(Message.MSG_ACCOUNT_REGIST_RESPONSE_S2C);
        short msglen = (short) (Config.MSG_LENGTH + msgno.length + respbody.length);

        ByteBuf byteBuf = Unpooled.buffer(msglen);
        byteBuf.writeBytes(ByteUtil.short2byte(msglen));
        byteBuf.writeBytes(msgno);
        byteBuf.writeBytes(respbody);

        doAccountRegistResponse(channel, byteBuf);
    }

    /**
     * 注册成功后，返回认证信息和服务器列表
     * @param channel
     * @param byteBuf
     */
    private void doAccountRegistResponse(Channel channel, ByteBuf byteBuf) {
        channel.writeAndFlush(byteBuf);
    }
}
