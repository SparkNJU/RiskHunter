import { axios } from '../utils/request'
import { CHAT_MODULE } from './_prefix'

/**
 * 聊天消息请求DTO
 */
export type ChatRequestDTO = {
    sessionId: number     // 会话ID
    message: string       // 用户输入的聊天内容
    userId: number        // 用户ID
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
 * 更新聊天会话标题
 * @param userId 用户ID
 * @param sessionId 会话ID
 * @param title 会话标题
 */
export const updateSessionTitle = (userId: number, sessionId: number, title: string) => {
    return axios.put(`${CHAT_MODULE}/session/${sessionId}/title`, null, {
        params: {
            userId,
            title
        }
    })
}

/**
 * 获取用户的所有会话ID
 * @param userId 用户ID
 */
export const getUserSessions = (userId: number) => {
    return axios.get(`${CHAT_MODULE}/sessions`, {
        params: {
            userId
        }
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

/**
 * 发送聊天消息（非流式响应）
 * @param chatRequestDTO 聊天请求对象
 */
export const sendMessageNoStream = (chatRequestDTO: ChatRequestDTO) => {
    return axios.post(`${CHAT_MODULE}/noStream`, chatRequestDTO, {
        timeout: 60000
    })
}

/**
 * 发送聊天消息（流式响应）
 * @param chatRequestDTO 聊天请求对象
 */

export const CHAT_STREAM_DEFAULT = `${axios.defaults.baseURL}${CHAT_MODULE}/stream`;
export const CHAT_STREAM_RAG = `${axios.defaults.baseURL}${CHAT_MODULE}/ragChat`;

/**
 * 删除聊天会话
 * @param sessionId 会话ID
 * @param userId 用户ID
 */
export const deleteSession = (sessionId: number, userId: number) => {
    return axios.delete(`${CHAT_MODULE}/session/${sessionId}`, {
      params: {
        userId
      }
    })
  }