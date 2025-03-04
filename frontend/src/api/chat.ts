import { axios } from '../utils/request'
import { CHAT_MODULE } from './_prefix'

/**
 * 聊天消息请求DTO
 */
type ChatRequestDTO = {
    sessionId: number     // 会话ID
    message: string       // 用户输入的聊天内容
    userId: number        // 用户ID
}

/**
 * 聊天记录实体
 */
export type ChatRecord = {
    id?: number           // 记录ID
    createTime?: string   // 创建时间
    userId: number        // 用户ID
    direction: boolean    // 方向，true表示用户发给大模型，false表示相反
    sessionId: number     // 会话ID
    content: string       // 聊天内容
}

/**
 * 创建新的聊天会话
 * @param userId 用户ID
 */
export const createSession = (userId: number) => {
    return axios.post(`${CHAT_MODULE}/session`, null, {
        params: {
            userId
        }
    })
}

/**
 * 发送聊天消息
 * @param chatRequestDTO 聊天请求对象
 */
export const sendMessage = (chatRequestDTO: ChatRequestDTO) => {
    return axios.post(`${CHAT_MODULE}/stream`, chatRequestDTO, {
        timeout: 120000
    })
}

/**
 * 获取指定会话的聊天历史记录
 * @param sessionId 会话ID
 * @param userId 用户ID
 */
export const getHistory = (sessionId: number, userId: number) => {
    return axios.get(`${CHAT_MODULE}/history/${sessionId}`, {
        params: {
            userId
        }
    })
}