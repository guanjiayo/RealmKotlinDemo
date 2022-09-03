package zs.xmx.realm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.realm.databinding.ActivityMainBinding
import zs.xmx.realm.model.Dog
import zs.xmx.realm.model.Person

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initData()
        initEvent(mBinding)
    }

    private fun initEvent(mBinding: ActivityMainBinding) {
        mBinding.actionInsert.setOnClickListener {
            insert()
        }
        mBinding.actionDeleteAll.setOnClickListener {
            RealmDatabase.get().deleteAll()
        }
        mBinding.actionDeleteFirst.setOnClickListener {
            RealmDatabase.get().deletePersonOfDogFirst()
        }
        mBinding.actionQuery.setOnClickListener {
            // val person = RealmDatabase.get().queryPersonByDogAge(1)
            val dogAgeMax = RealmDatabase.get().queryDogMaxAge(0)
            val dogAgeMin = RealmDatabase.get().queryDogMinAge(0)
            val dogAgeAvg = RealmDatabase.get().queryDogAvgAge(0)
            Log.i(
                "TTTT", "\nmax: $dogAgeMax \n" +
                        "min: $dogAgeMin \n" +
                        "avg: $dogAgeAvg"
            )
        }
    }

    private fun initData() {
        RealmDatabase.get().deleteAll()

        val person = Person().apply {
            name = "张三"
            age = 10
            gender = "男"
        }


        RealmDatabase.get().insertPerson(person)

        for (i in 1..5) {
            val dog = Dog()
            dog.id = person.id
            dog.name = "Bob $i"
            dog.age = i
            RealmDatabase.get().insertDog(dog)
        }
    }

    private fun insert() {
        val person = Person()
        person.name = "李四"
        person.age = 18
        person.gender = "女"

        RealmDatabase.get().insertPerson(person)

        for (i in 1..3) {
            val dog = Dog()
            dog.id = person.id
            dog.name = "MM $i"
            dog.age = i
            RealmDatabase.get().insertDog(dog)
        }
    }


}