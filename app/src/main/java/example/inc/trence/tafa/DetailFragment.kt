package example.inc.trence.tafa


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    var count:Int = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val theDetail:detail?=arguments?.getParcelable("detailBundle")

        Glide.with(view.context).load(theDetail?.img).into(detail_img)
        detail_username.text = theDetail?.username
        detail_body.text = theDetail?.body
        detail_title.text = theDetail?.title

        detail_num_comment.text = "Number of Comments : Processing .."
        val comments = arguments?.getString("comments")
        val request1 = JsonArrayRequest(Request.Method.GET,
                comments,
                null,
                Response.Listener<JSONArray> { it ->
                    for(i in 0 until it.length() - 1){
                      val o = it.get(i) as JSONObject;
                        if(o.getInt("postId") == theDetail?.id){
                            count++
                        }
                    }
                    detail_num_comment.text = "Number of Comments : "+ count
                },
                Response.ErrorListener {
                    Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show()
                })
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request1)
    }


}
