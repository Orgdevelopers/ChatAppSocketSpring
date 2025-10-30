package com.kulvinder.livechat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private String from; // Username of the sender
    private String text; // Message content

    @Builder.Default
    private String animation = "none";

    @Builder.Default
    private Boolean confettie = false;

    @Builder.Default
    private Boolean broadcast = false;

    
}
