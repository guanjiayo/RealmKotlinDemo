package zs.xmx.realm.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.*


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *
 */
class Person : RealmObject {
    //主键类型必须是String或者整型(byte, short, int, long)
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var name: String = ""
    var age: Int = 0
    var gender: String = ""
    var dogList: RealmList<Dog> = realmListOf()

    override fun toString(): String {
        return "Person(name='$name', age=$age, gender='$gender')"
    }


}

class Dog : RealmObject {
    var id: String = ""
    var name: String = ""
    var age: Int = 0

    override fun toString(): String {
        return "Dog(name='$name', age=$age)"
    }


}