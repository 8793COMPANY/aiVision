import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.R
import com.corporation8793.aivision.card.CardAdapter
import com.corporation8793.aivision.card.CardAdapter.Companion.MAX_ELEVATION_FACTOR
import com.corporation8793.aivision.card.CardItem
import com.corporation8793.aivision.databinding.CardAdaptorBinding
import com.corporation8793.aivision.fragment.CourseFragment
import com.corporation8793.aivision.fragment.MyFragment

class CardPagerAdapter(val context: Context, val activity: MainActivity): CardAdapter, PagerAdapter(){
    private var mViews: MutableList<CardView> = mutableListOf()
    private var mData: MutableList<CardItem> = mutableListOf()
    private lateinit var binding : CardAdaptorBinding
    private var mBaseElevation = 0f

    override fun getBaseElevation(): Float {
        return mBaseElevation
    }

    override fun getCardViewAt(position: Int): CardView {
        return mViews[position]
    }

    fun addCardItem(item: CardItem) {
        mData.add(item)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater

        binding = CardAdaptorBinding.inflate(inflater)
        if (mData[position].getCourseType() == 0) {
            binding.mCourseName.text = mData[position].getCourseName()
            binding.mCourseTime.text = mData[position].getCourseTime()
            binding.mCourseStart.text = mData[position].getCourseStart()
            binding.mCourseGoal.text = mData[position].getCourseGoal()
            binding.mCourseDistance.text = mData[position].getCourseDistance()
            binding.mySpotImg.visibility = View.INVISIBLE
            binding.mySpotText.visibility = View.INVISIBLE

            binding.courseStartBtn.setOnClickListener {
                // TODO : 시작하기 클릭 처리 리스너
                activity.replaceFragment(CourseFragment(this.activity, position), 2)
            }

            binding.cardCourseStarSelector.setOnClickListener {
                // TODO : 별 클릭 처리 리스너
                getCardViewAt(position).isSelected = !getCardViewAt(position).isSelected
            }
        } else {
            binding.cardCourseStarSelector.isSelected = mData[position].getCourseStar()
            binding.mCourseName.text = mData[position].getCourseName()
            binding.mCourseTime.visibility = View.INVISIBLE
            binding.mCourseStart.visibility = View.INVISIBLE
            binding.mCourseGoal.visibility = View.INVISIBLE
            binding.mCourseDistance.visibility = View.INVISIBLE
            binding.startSpotImg.visibility = View.INVISIBLE
            binding.goalSpotImg.visibility = View.INVISIBLE
            binding.cardDistance.visibility = View.INVISIBLE
            binding.courseStartBtn.setBackgroundResource(R.drawable.course_start_btn_2)
            binding.mySpotImg.visibility = View.VISIBLE
            binding.mySpotText.visibility = View.VISIBLE

            binding.courseStartBtn.setOnClickListener {
                // TODO : 시작하기 클릭 처리 리스너
                activity.replaceFragment(MyFragment(activity), 3)
            }

            binding.cardCourseStarSelector.setOnClickListener {
                // TODO : 별 클릭 처리 리스너
                getCardViewAt(position).isSelected = !getCardViewAt(position).isSelected
            }
        }

        binding.cardView.maxCardElevation = mBaseElevation * MAX_ELEVATION_FACTOR

        if (mBaseElevation == 0f) {
            mBaseElevation = binding.cardView.cardElevation
        }

        binding.cardView.maxCardElevation = mBaseElevation * MAX_ELEVATION_FACTOR

        mViews.add(binding.cardView)
        container.addView(binding.root)

        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int { return mData.size }

    fun getRegisteredView(position: Int): CardView? { return mViews[position] } }