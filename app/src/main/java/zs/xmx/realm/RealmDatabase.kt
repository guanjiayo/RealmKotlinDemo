package zs.xmx.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.max
import io.realm.kotlin.query.min
import io.realm.kotlin.query.sum
import zs.xmx.realm.model.Dog
import zs.xmx.realm.model.Person

class RealmDatabase private constructor() {

    private val mRealm: Realm by lazy {
        val configuration = RealmConfiguration.Builder(
            schema = setOf(
                Person::class,
                Dog::class
            )
        )
            .name(DATABASE_NAME)
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.open(configuration)
    }

    fun getRealm(): Realm {
        return mRealm
    }

    private var mPerson: Person? = null
    fun insertPerson(person: Person) {
        mRealm.writeBlocking {
            mPerson = copyToRealm(person)
        }
    }

    fun insertDog(dog: Dog) {
        mRealm.writeBlocking {
            val dog1 = copyToRealm(dog)
            mPerson?.dogList?.add(dog1)
        }
    }

    /**
     * 删除第一个Person里的第一个Dog
     */
    fun deletePersonOfDogFirst() {
        mRealm.writeBlocking {
            val personList = query<Person>().find()
            findLatest(personList[0].dogList[0])?.let { delete(it) }
        }
    }

    fun deleteAll() {
        mRealm.writeBlocking {
            delete(query<Person>())
            delete(query<Dog>())
        }
    }

    /**
     * 查询 Dog age 等于"几"的 person
     */
    fun queryPersonByDogAge(age: Int): RealmResults<Person> {
        return mRealm.query<Person>("dogList.age == $0", age).find()
    }

    /**
     * 查询第一个Person的Dog --- age的最大值
     */
    fun queryDogMaxAge(position: Int): Int? {
        val personId = mRealm.query<Person>().find()[position].id
        return mRealm.query<Dog>("id == $0", personId).max<Int>("age").find()
    }

    /**
     * 查询第一个Person的Dog --- age的最小值
     */
    fun queryDogMinAge(position: Int): Int? {
        val personId = mRealm.query<Person>().find()[position].id
        return mRealm.query<Dog>("id == $0", personId).min<Int>("age").find()
    }

    /**
     * 查询第一个Person的Dog --- age的平均值
     */
    fun queryDogAvgAge(position: Int): Double {
        val personId = mRealm.query<Person>().find()[position].id
        val dogList = mRealm.query<Dog>("id == $0", personId)
        return (dogList.sum<Int>("age").find() / dogList.count().find()).toDouble()
    }

    companion object {
        private const val DATABASE_NAME = "roller_database.realm"

        @Volatile
        private var INSTANCE: RealmDatabase? = null

        fun get(): RealmDatabase {
            return INSTANCE ?: synchronized(this) {
                return INSTANCE ?: RealmDatabase().also { INSTANCE = it }
            }
        }
    }
}