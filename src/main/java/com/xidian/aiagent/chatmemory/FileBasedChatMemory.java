package com.xidian.aiagent.chatmemory;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件的对话记忆实现类
 * 实现ChatMemory接口，通过Kryo序列化将Message列表持久化到本地文件
 */
public class FileBasedChatMemory implements ChatMemory {

    // 对话文件存储的根目录（不可修改）
    private final String BASE_DIR;
    // 全局Kryo序列化实例（静态单例避免重复创建）
    private static final Kryo kryo = new Kryo();

    // 静态代码块：初始化Kryo配置
    static {
        // 关闭强制注册类，支持Message接口多子类序列化
        kryo.setRegistrationRequired(false);
        // 设置实例化策略，解决Message子类无无参构造的问题
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    /**
     * 构造函数：初始化存储根目录
     * @param dir 存储目录路径
     */
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        // 目录不存在则递归创建
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /**
     * 添加消息到指定会话的记忆中
     * @param conversationId 会话ID
     * @param messages 要添加的消息列表
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
        // 获取该会话已有的消息（无则创建空列表）
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        // 追加新消息
        conversationMessages.addAll(messages);
        // 持久化更新后的消息列表
        saveConversation(conversationId, conversationMessages);
    }

    /**
     * 获取指定会话的最后N条消息
     * @param conversationId 会话ID
     * @param lastN 要获取的最后N条消息数量
     * @return 最后N条消息列表
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 获取该会话的所有消息
        List<Message> allMessages = getOrCreateConversation(conversationId);
        // 截取最后N条消息（避免跳过数为负数）
        return allMessages.stream()
                .skip(Math.max(0, allMessages.size() - lastN))
                .toList();
    }

    /**
     * 清空指定会话的记忆（删除对应文件）
     * @param conversationId 会话ID
     */
    @Override
    public void clear(String conversationId) {
        // 获取会话对应的文件
        File file = getConversationFile(conversationId);
        // 文件存在则删除
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 私有方法：获取或创建会话的消息列表
     * 文件存在则反序列化读取，不存在则返回空列表
     * @param conversationId 会话ID
     * @return 会话的消息列表
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        // 获取会话对应的文件对象
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        // 文件存在则读取
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                // Kryo反序列化：字节流转Message列表
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                // 异常打印栈轨迹（生产环境建议替换为日志）
                e.printStackTrace();
            }
        }
        return messages;
    }

    /**
     * 私有方法：保存会话消息列表到文件
     * @param conversationId 会话ID
     * @param messages 要保存的消息列表
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        // 获取会话对应的文件对象
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            // Kryo序列化：Message列表转字节流写入文件
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            // 异常打印栈轨迹（生产环境建议替换为日志）
            e.printStackTrace();
        }
    }

    /**
     * 私有方法：生成会话对应的文件对象
     * 文件名格式：BASE_DIR/conversationId.kryo
     * @param conversationId 会话ID
     * @return 会话对应的文件
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}