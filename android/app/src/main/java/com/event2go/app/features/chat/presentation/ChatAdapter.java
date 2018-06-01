package com.event2go.app.features.chat.presentation;

import android.databinding.BaseObservable;

import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;
import com.event2go.app.R;
import com.event2go.app.databinding.ListItemChatMessageBinding;
import com.event2go.app.features.chat.data.ChatMessage;

/**
 * Created by Iliya Gogolev on 3/18/2016.
 */
public class ChatAdapter extends BaseRecyclerAdapter<ChatMessage> {

//    private final List<ChatMessage> chatMessages;
//    private Activity listener;

//    public ChatAdapter(Activity listener, List<ChatMessage> chatMessages) {
//        this.listener = listener;
//        this.chatMessages = chatMessages;
//    }

//    @Override
//    public int getCount() {
//        if (chatMessages != null) {
//            return chatMessages.size();
//        } else {
//            return 0;
//        }
//    }

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_chat_message;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item) {
        ListItemChatMessageBinding bind = (ListItemChatMessageBinding) holder.binding;
        bind.setMessage((ChatMessage) item);
    }


//    @Override
//    public ChatMessage getItem(int position) {
//        if (chatMessages != null) {
//            return chatMessages.get(position);
//        } else {
//            return null;
//        }
//    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        holder.
//        ListItemChatMessageBinding bind = DataBindingUtil.bind(parent);
//
//        holder. bind.setMessage(chatMessage);
//
//        boolean myMsg = chatMessage.getIsMe() ;//Just a dummy check
//        //to simulate whether it me or other sender
//        setAlignment(holder, myMsg);
//    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @Override
//    public View getListener(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        ChatMessage chatMessage = getItem(position);
//        LayoutInflater vi = (LayoutInflater) listener.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        if (convertView == null) {
//            convertView = vi.inflate(R.layout.list_item_chat_message, null);
//            holder = createViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        ListItemChatMessageBinding bind = DataBindingUtil.bind(parent);
//
//        bind.setMessage(chatMessage);
//
//        boolean myMsg = chatMessage.getIsMe() ;//Just a dummy check
//        //to simulate whether it me or other sender
//        setAlignment(holder, myMsg);
//        //holder.txtMessage.setText(chatMessage.getMessage());
//        //holder.txtInfo.setText(chatMessage.getDate());
//
//        return convertView;
//    }

//    public void add(ChatMessage message) {
//        super.add(message);
//    }

//    public void add(List<ChatMessage> messages) {
//        .addAll(messages);
//    }

//    private void setAlignment(ViewHolder holder, boolean isMe) {
//        if (!isMe) {
//            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
//
//            LinearLayout.LayoutParams layoutParams =
//                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            holder.contentWithBG.setLayoutParams(layoutParams);
//
//            RelativeLayout.LayoutParams lp =
//                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
//            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            holder.content.setLayoutParams(lp);
//            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            holder.txtMessage.setLayoutParams(layoutParams);
//
//            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            holder.txtInfo.setLayoutParams(layoutParams);
//        } else {
//            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);
//
//            LinearLayout.LayoutParams layoutParams =
//                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
//            layoutParams.gravity = Gravity.LEFT;
//            holder.contentWithBG.setLayoutParams(layoutParams);
//
//            RelativeLayout.LayoutParams lp =
//                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
//            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            holder.content.setLayoutParams(lp);
//            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
//            layoutParams.gravity = Gravity.LEFT;
//            holder.txtMessage.setLayoutParams(layoutParams);
//
//            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
//            layoutParams.gravity = Gravity.LEFT;
//            holder.txtInfo.setLayoutParams(layoutParams);
//        }
//    }

//    private ViewHolder createViewHolder(View v) {
//        ViewHolder holder = new ViewHolder();
//        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
//        holder.content = (LinearLayout) v.findViewById(R.id.content);
//        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
//        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
//        return holder;
//    }

//    private static class ViewHolder {
//
//        public ViewHolder() {
//
//        }
//
//        public TextView txtMessage;
//        public TextView txtInfo;
//        public LinearLayout content;
//        public LinearLayout contentWithBG;
//    }
}