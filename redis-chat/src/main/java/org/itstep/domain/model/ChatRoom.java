package org.itstep.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.itstep.domain.status.ChatRoomStatus;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 34789214329287934L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(value = AccessLevel.PRIVATE)
    private String name;

    @Setter(value = AccessLevel.PRIVATE)
    @Enumerated(value = EnumType.ORDINAL)
    private ChatRoomStatus status = ChatRoomStatus.ACTIVE;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ChatMessage> messages = new ArrayList<>();

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

    public static ChatRoomBuilder getBuilder() {
        return new ChatRoomBuilder();
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    public static class ChatRoomBuilder {
        private String name;

        public ChatRoomBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ChatRoom build() {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setName(name);
            return chatRoom;
        }
    }
}
