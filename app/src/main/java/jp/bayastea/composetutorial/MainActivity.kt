package jp.bayastea.composetutorial

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.bayastea.composetutorial.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTutorialTheme {
//                    MessageCard(Message("Anddroid", "Jetpack Compose"))
                    // 単に1つのMessageCardを表示していたのをConversationを呼ぶことでリスト表示にする
                    Conversation(SampleData.conversationSample)
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {

    // メッセージの周りにパディング設定
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // 画像サイズを40dpに設定
                .size(40.dp)
                // 画像を角丸に設定
                .clip(CircleShape)
                .border(
                    1.5.dp, MaterialTheme.colors.secondary, CircleShape
                )
        )

        // 画像とColumnの間に横方向スペースを挿入
        Spacer(modifier = Modifier.width(8.dp))

        // 展開されたかどうか
        // rememberによって状態がメモリへ保存され、mutableStateOfの中身の変更を検知できる
        // 変更が検知されたらUIが自動的に更新される（= おそらくLiveDataのようなイメージ）
        var isExpanded by remember { mutableStateOf(false) }

        // surfaceColorはある色から別の色へ徐々に更新される
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        // clickableでクリックイベントを処理する（= setOnClickLister的な）
        // クリックすることでisExpandedの状態を変更する
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
        // 文面
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            // authorとbodyの間に縦方向のスペースを挿入
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColorはPrimarilyからsurfaceへと徐々に変わる
                color = surfaceColor,
                // animateContentSizeはSurfaceのサイズを徐々に変化させる
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2,
                    // メッセージが展開されていれば全て表示し、そうでなければ1行のみ表示する
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                )
            }
        }
    }
}

//@Preview(name = "Light Mode") // 同じ関数に複数のアノテーションを追加できる
//@Preview(
//    // MaterialDesignのサポートで自動でダークテーマの設定がされる
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode" // nameはプレビューを区別するための名前
//)
//
// TODO: このメソッドが有効化されたままだとビルドしてもこの内容が表示される
//@Preview
//@Composable
//fun PreviewMessageCard() {
//    ComposeTutorialTheme {
//        Surface(modifier = Modifier.fillMaxSize()) {
//            MessageCard(
//                msg = Message("Colleague", "Hey, take a look at Jetpack Compose, it's great!")
//            )
//        }
//    }
//}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message -> // messageのListがitemsの引数に渡され、ラムダでは1つずつ処理される
            MessageCard(message)
        }
    }
}


@Preview
@Composable
fun PreviewConversation() {
    ComposeTutorialTheme {
        Conversation(SampleData.conversationSample)
    }
}