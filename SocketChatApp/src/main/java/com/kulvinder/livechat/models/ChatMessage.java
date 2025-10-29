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
    private String animation;
    private Boolean confettie;

    // ✅ Getters & Setters (required by Spring to map JSON automatically)
    // public String getFrom() {
    //     return from;
    // }

    // public void setFrom(String from) {
    //     this.from = from;
    // }

    // public String getText() {
    //     return text;
    // }

    // public void setText(String text) {
    //     this.text = text;
    // }

    // public String getAnimation() {
    //     return animation;
    // }

    // public void setAnimation(String animation) {
    //     this.animation = animation;
    // }

    // public Boolean isConfettie() {
    //     return confettie;
    // }

    // public void setConfettie(Boolean confettie) {
    //     this.confettie = confettie;
    // }
}
