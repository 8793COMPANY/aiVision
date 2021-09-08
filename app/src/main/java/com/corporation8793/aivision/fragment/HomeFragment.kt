package com.corporation8793.aivision.fragment

import CardPagerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.corporation8793.aivision.MainActivity
import com.corporation8793.aivision.card.CardAdapter
import com.corporation8793.aivision.card.CardItem
import com.corporation8793.aivision.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment(activity: MainActivity) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var mActivity = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        var binding = FragmentHomeBinding.inflate(inflater, container, false)
        val testCardAdapter = CardPagerAdapter(requireActivity().applicationContext, mActivity)

        // 카드뷰 뷰페이저 데이터 입력
        testCardAdapter.addCardItem( CardItem(0, "횃불코스", "1시간 50분", "전남대학교 정문", "구 전남도청", "6.7km", false))
        testCardAdapter.addCardItem( CardItem(0, "희생코스", "5시간 20분", "구 전남도청", "광주공원 광장", "21.5km", false))
        testCardAdapter.addCardItem( CardItem(0, "광장코스", "3시간", "광주공원 광장", "5·18기념공원", "11.1km", false))
        testCardAdapter.addCardItem( CardItem(0, "열정코스", "1시간 30분", "5·18기념공원", "전남대 민주길", "5.9km", false))
        testCardAdapter.addCardItem( CardItem(0, "영혼코스", "3시간 10분", "전남대학교 정문", "국립5·18민주묘지", "12.5km", false))
        testCardAdapter.addCardItem( CardItem(1, "나만의 VR코스", "0", "0", "0", "0", false))

        var mLastOffset = 0f
        binding.cardViewPager.adapter = testCardAdapter
        binding.cardViewPager.offscreenPageLimit = 6
        binding.cardViewPager.currentItem = 0

        binding.cardViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val realCurrentPosition: Int
                val nextPosition: Int
                val baseElevation: Float = (binding.cardViewPager.adapter as CardPagerAdapter).getBaseElevation()
                val realOffset: Float
                val goingLeft: Boolean = mLastOffset > positionOffset

                if (goingLeft) {
                    realCurrentPosition = position + 1
                    nextPosition = position
                    realOffset = 1 - positionOffset
                } else {
                    nextPosition = position + 1
                    realCurrentPosition = position
                    realOffset = positionOffset
                }

                if (nextPosition > (binding.cardViewPager.adapter as CardPagerAdapter).count - 1
                    || realCurrentPosition > (binding.cardViewPager.adapter as CardPagerAdapter).count - 1) {
                        return
                }

                val currentCard: CardView = (binding.cardViewPager.adapter as CardPagerAdapter).getCardViewAt(realCurrentPosition)

                currentCard.scaleX = (1 + 0.1 * (1 - realOffset)).toFloat()
                currentCard.scaleY = (1 + 0.1 * (1 - realOffset)).toFloat()

                currentCard.cardElevation = baseElevation + (baseElevation * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset))

                val nextCard: CardView = (binding.cardViewPager.adapter as CardPagerAdapter).getCardViewAt(nextPosition)

                nextCard.scaleX = (1 + 0.1 * realOffset).toFloat()
                nextCard.scaleY = (1 + 0.1 * realOffset).toFloat()

                nextCard.cardElevation = baseElevation + (baseElevation * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * realOffset)
                mLastOffset = positionOffset
            }

            override fun onPageSelected(position: Int) {

            } })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(): Companion {
            return this
        }
    }
}