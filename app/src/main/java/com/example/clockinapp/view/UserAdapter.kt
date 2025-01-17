package com.example.clockinapp.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
//import android.widget.ImageView
//import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clockinapp.R
import com.example.clockinapp.model.UserData


class UserAdapter(val c:Context, val userList:ArrayList<UserData>): RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{
    inner class UserViewHolder(val v: View): RecyclerView.ViewHolder(v)
    {
        var name :TextView
        var mbNum :TextView
        var mMenus: ImageView
        init
        {
            name = v.findViewById<TextView>(R.id.mTitle)
            mbNum = v.findViewById<TextView>(R.id.mSubTitle)
            mMenus = v.findViewById(R.id.mMenus)
            mMenus.setOnClickListener{ popupMenus(it)}
        }

        private fun popupMenus(v: View) {
            val position = userList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.editText->
                        {
                            val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)
                            val name = v.findViewById<EditText>(R.id.ProjectName)
                            val number = v.findViewById<EditText>(R.id.Description)
                                    AlertDialog.Builder(c)
                                    .setView(v)
                                    .setPositiveButton("Ok"){
                                            dialog,_->
                                            //userName = Project Name, userMb = description
                                            position.userName = name.text.toString()
                                            position.userMb = number.text.toString()
                                            notifyDataSetChanged()
                                            Toast.makeText(c,"Entry Information is Edited",Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
                                    }
                                    .setNegativeButton("Cancel"){
                                            dialog,_->
                                            dialog.dismiss()
                                    }
                                    .create()
                                    .show()

                            true}
                    R.id.delete->
                    {
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setIcon(R.drawable.warning)
                            .setMessage("Are you sure delete this Entry")
                            .setPositiveButton("Yes")
                            {
                                    dialog,_->
                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()

                                Toast.makeText(c,"Deleted this Entry",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No")
                            {
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newlist = userList[position]
        holder.name.text = newlist.userName
        holder.mbNum.text = newlist.userMb
    }
}