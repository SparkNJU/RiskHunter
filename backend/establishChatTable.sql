CREATE TABLE chat_record (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                             session_id BIGINT NOT NULL COMMENT '会话ID',
                             user_id BIGINT NOT NULL COMMENT '用户ID',
                             direction BOOLEAN NOT NULL COMMENT '消息方向：true=用户消息，false=AI消息',
                             content TEXT NOT NULL COMMENT '消息内容',
                             create_time DATETIME NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天记录表';

CREATE TABLE chat_session (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                              user_id BIGINT NOT NULL COMMENT '用户ID',
                              create_time DATETIME NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天会话表';

CREATE INDEX idx_session_id ON chat_record (session_id);
CREATE INDEX idx_user_id ON chat_record (user_id);
CREATE INDEX idx_user_id ON chat_session (user_id);