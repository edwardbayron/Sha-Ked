package org.steamzone.shaked.box

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class LoginBox {
    @Id(assignable = true)
    var id: Long = 0
    var email: String? = null
    var password: String? = null

    companion object {
        fun save(list: List<LoginBox>): List<LoginBox> {
            SBox.getBox(LoginBox::class.java).put(list)
            return list
        }

        fun save(model: LoginBox): LoginBox {
            SBox.getBox(LoginBox::class.java).put(model)
            return model
        }

        fun delete(list: List<LoginBox>): List<LoginBox> {
            SBox.getBox(LoginBox::class.java).remove(list)
            return list
        }

        fun delete(model: LoginBox): LoginBox {
            SBox.getBox(LoginBox::class.java).remove(model)
            return model
        }

        fun get(id: Long): LoginBox? {

            return SBox.getBox(LoginBox::class.java).query().equal(LoginBox_.id, id).build().findFirst()
        }

        fun get(): LoginBox? {

            return get(1)

        }
    }

}