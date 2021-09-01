import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.corporation8793.aivision.card.CardAdapter
import com.corporation8793.aivision.card.CardAdapter.Companion.MAX_ELEVATION_FACTOR
import com.corporation8793.aivision.card.CardItem
import com.corporation8793.aivision.databinding.CardAdaptorBinding

class CardPagerAdapter(val context: Context): CardAdapter, PagerAdapter(){
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
        binding.mCourseName.text = mData[position].getCourseName()

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