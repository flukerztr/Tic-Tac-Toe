import java.util.Scanner;

public class TicTacToe {

    // กระดานของเกม (ขนาด 3x3)
    static char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}; 
    static char player = 'X'; // ผู้เล่นจะเล่นเป็น X
    static char bot = 'O'; // บอทจะเล่นเป็น O

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // สร้างออบเจกต์ Scanner เพื่อรับอินพุตจากผู้เล่น
        printBoard(); // แสดงกระดานเริ่มต้น

        // ลูปหลักของเกม
        while (true) {
            playerTurn(scanner); // ผู้เล่นทำการเลือกช่อง
            if (isGameOver()) break; // ตรวจสอบว่าผู้เล่นหรือบอทชนะหรือเสมอหรือยัง

            botTurn(); // บอททำการเลือกช่อง
            if (isGameOver()) break; // ตรวจสอบอีกครั้งหลังจากบอทเล่น
        }
        scanner.close(); // ปิดการใช้ Scanner
    }

    // ฟังก์ชันสำหรับตาของผู้เล่น
    public static void playerTurn(Scanner scanner) {
        int move;
        while (true) {
            System.out.println("Choose your move (1-9): "); // ขอให้ผู้เล่นเลือกตำแหน่ง
            move = scanner.nextInt() - 1; // รับค่าจากผู้เล่นและลดค่าลงเพื่อให้ตรงกับ index ใน array
            if (move >= 0 && move < 9 && board[move] == ' ') { // ตรวจสอบว่าตำแหน่งที่เลือกว่างอยู่
                board[move] = player; // กำหนดให้ X (ผู้เล่น) ลงในตำแหน่งที่เลือก
                break;
            }
            System.out.println("This position is already occupied. Please choose another!"); // แจ้งเตือนหากเลือกช่องที่มีคนเล่นไปแล้ว
        }
        printBoard(); // แสดงกระดานหลังจากผู้เล่นเล่น
    }

    // ฟังก์ชันสำหรับตาของบอท
    public static void botTurn() {
        System.out.println("Bot is making its move..."); // แจ้งให้ผู้เล่นทราบว่าบอทกำลังคิด
        int bestMove = minimax(true); // ใช้ Minimax algorithm เพื่อหาตำแหน่งที่ดีที่สุดที่บอทจะเล่น
        board[bestMove] = bot; // บอททำการเล่น O ในตำแหน่งที่ดีที่สุด
        printBoard(); // แสดงกระดานหลังจากบอทเล่น
    }

    // ฟังก์ชัน Minimax เพื่อประเมินการเคลื่อนที่ที่ดีที่สุด
    public static int minimax(boolean isBotTurn) {
        int bestScore = isBotTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE; // กำหนดคะแนนเริ่มต้น (บอทจะพยายามหาคะแนนสูงสุด, ผู้เล่นจะพยายามหาคะแนนต่ำสุด)
        int bestMove = -1; // ค่าเริ่มต้นสำหรับตำแหน่งที่ดีที่สุด

        // ลูปเพื่อตรวจสอบทุกช่องที่ว่าง
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') { // ตรวจสอบเฉพาะช่องที่ยังว่าง
                board[i] = isBotTurn ? bot : player; // ลองวาง X หรือ O ลงไปตามลำดับ
                int score = minimaxEvaluate(!isBotTurn); // ประเมินคะแนนของแต่ละการเคลื่อนที่
                board[i] = ' '; // คืนค่ากระดานให้กลับมาเหมือนเดิม
                // อัปเดตคะแนนและตำแหน่งที่ดีที่สุดสำหรับบอทหรือผู้เล่น
                if (isBotTurn && score > bestScore) { 
                    bestScore = score;
                    bestMove = i;
                } else if (!isBotTurn && score < bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove; // ส่งคืนตำแหน่งที่ดีที่สุด
    }

    // ฟังก์ชันประเมินคะแนนของการเคลื่อนที่สำหรับ Minimax
    public static int minimaxEvaluate(boolean isBotTurn) {
        if (checkWin(bot)) return 1; // ถ้าบอทชนะ ส่งคะแนน 1
        if (checkWin(player)) return -1; // ถ้าผู้เล่นชนะ ส่งคะแนน -1
        if (isBoardFull()) return 0; // ถ้ากระดานเต็ม ส่งคะแนน 0 (เสมอ)

        int bestScore = isBotTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE; // ตั้งค่าคะแนนเริ่มต้น

        // ลูปเพื่อตรวจสอบทุกช่องที่ว่าง
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = isBotTurn ? bot : player; // ลองวาง X หรือ O ลงไปตามลำดับ
                int score = minimaxEvaluate(!isBotTurn); // ประเมินคะแนนของแต่ละการเคลื่อนที่
                board[i] = ' '; // คืนค่ากระดานให้กลับมาเหมือนเดิม
                // อัปเดตคะแนนที่ดีที่สุด
                if (isBotTurn) {
                    bestScore = Math.max(score, bestScore);
                } else {
                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore; // ส่งคะแนนที่ดีที่สุด
    }

    // ฟังก์ชันตรวจสอบว่ามีใครชนะหรือไม่
    public static boolean checkWin(char c) {
        // ตรวจสอบเงื่อนไขชนะทุกแนว (แนวนอน, แนวตั้ง, แนวทแยง)
        return (board[0] == c && board[1] == c && board[2] == c) ||
               (board[3] == c && board[4] == c && board[5] == c) ||
               (board[6] == c && board[7] == c && board[8] == c) ||
               (board[0] == c && board[3] == c && board[6] == c) ||
               (board[1] == c && board[4] == c && board[7] == c) ||
               (board[2] == c && board[5] == c && board[8] == c) ||
               (board[0] == c && board[4] == c && board[8] == c) ||
               (board[2] == c && board[4] == c && board[6] == c);
    }

    // ฟังก์ชันตรวจสอบว่ากระดานเต็มหรือไม่ (ใช้เพื่อตรวจสอบว่าการเล่นเสมอหรือไม่)
    public static boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') return false; // ถ้ายังมีช่องว่างอยู่ ให้ส่งคืนค่า false
        }
        return true; // ถ้าทุกช่องเต็มแล้ว ให้ส่งคืนค่า true
    }

    // ฟังก์ชันตรวจสอบผลการเล่น
    public static boolean isGameOver() {
        if (checkWin(player)) { // ตรวจสอบว่าผู้เล่นชนะหรือไม่
            System.out.println("Player wins!");
            return true;
        }
        if (checkWin(bot)) { // ตรวจสอบว่าบอทชนะหรือไม่
            System.out.println("Bot wins!");
            return true;
        }
        if (isBoardFull()) { // ตรวจสอบว่ากระดานเต็มและเสมอหรือไม่
            System.out.println("It's a draw!");
            return true;
        }
        return false; // ถ้ายังไม่มีใครชนะและกระดานยังไม่เต็ม ให้ส่งคืนค่า false
    }

    // ฟังก์ชันแสดงกระดานเกม
    public static void printBoard() {
        System.out.println(board[0] + "|" + board[1] + "|" + board[2]); // แสดงแถวที่ 1
        System.out.println("-+-+-"); // แสดงเส้นแบ่ง
        System.out.println(board[3] + "|" + board[4] + "|" + board[5]); // แสดงแถวที่ 2
        System.out.println("-+-+-"); // แสดงเส้นแบ่ง
        System.out.println(board[6] + "|" + board[7] + "|" + board[8]); // แสดงแถวที่ 3
    }
}