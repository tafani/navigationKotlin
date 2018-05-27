package example.inc.trence.tafa


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_start.*
import org.json.JSONArray
import org.json.JSONObject


class StartFragment : Fragment() {

    private var Users: JSONArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val post = arguments?.getString("post")
        val users = arguments?.getString("users")
        Post_list.layoutManager = LinearLayoutManager(context)

        val request1 = JsonArrayRequest(Request.Method.GET,
                users,
                null,
                Response.Listener<JSONArray> { it ->
                    Users = it
                },
                Response.ErrorListener {
                    Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show()
                })
        val request2 = JsonArrayRequest(Request.Method.GET,
                post,
                null,
                Response.Listener<JSONArray>{
                    it->
                    Post_list.adapter = NewAdapter(it, this.Users!!)
                },
                Response.ErrorListener {})


        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request1)
        requestQueue.add(request2)
      }


    class NewAdapter(val post: JSONArray,val users:JSONArray) : RecyclerView.Adapter<PostViewHolder>() {
        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.bind(post.getJSONObject(position), position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.postcard, parent, false)
            return PostViewHolder(view, users)

        }

        override fun getItemCount(): Int = post.length()
    }
    class PostViewHolder(val view: View, val users: JSONArray) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceAsColor")
        fun bind(newsItem: JSONObject, position: Int) {
            val t1 = view.findViewById(R.id.title) as TextView
            val t2 = view.findViewById(R.id.username) as TextView
            val t3 = view.findViewById(R.id.user_img) as CircleImageView
            val bk = view.findViewById(R.id.postcard) as ConstraintLayout
            if(position % 2 != 0)
            bk.setBackgroundColor(R.color.material_blue_grey_800)
            val obj = users.get(newsItem.getInt("userId") - 1) as JSONObject
            val s = "http://api.adorable.io/avatars/285/" + obj.get("email")+".png"
            Glide.with(view.context).load(s).into(t3)
            t1.text = newsItem.getString("title")
            t2.text = obj.getString("name")

            view.setOnClickListener{

                val arg = Bundle().apply {
                    putParcelable("detailBundle",
                                    detail(newsItem.getInt("id"),
                                            newsItem.getString("title"),
                                            newsItem.getString("body"),
                                            obj.getString("name"),
                                            s,
                                            obj.getString("username")
                                    ))
                }
                it.findNavController().navigate(R.id.toDetail,arg)
            }
        }

    }
}
