package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btlandroid.Account;
import com.example.btlandroid.R;

import java.util.List;

import Activity.AccountManage;

public class AccountAdapter extends ArrayAdapter<Account>{
    private AccountManage context;
    private int layout;
    List<Account> accountList;
    public AccountAdapter(AccountManage context, int layout, List<Account> accountList) {
        super(context,layout,accountList);
    }
    private class ViewHolder{
        ImageView imgDelete;
        TextView txtUser, txtEmail;
    }
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onImageClick(Account item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccountAdapter.ViewHolder holder;
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.account_lists, parent, false);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_lists, parent, false);
            holder = new ViewHolder();
            holder.txtUser = convertView.findViewById(R.id.accountUser);
            holder.txtEmail = convertView.findViewById(R.id.accountEmail);
            holder.imgDelete = convertView.findViewById(R.id.imgDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Account account = getItem(position);
        holder.txtUser.setText(account.getTenDangNhap());
        holder.txtEmail.setText(account.getEmail());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick(getItem(position));
                }
            }
        });
        return convertView;
    }
}
