package com.lancer.chat.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message extends BaseEntity{



    String senderId;
    String content;

    @Field("timestamp")
    Instant timestamp;

    @Field("attachments")
    List<String> attachments;
}
