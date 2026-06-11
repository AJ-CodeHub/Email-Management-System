package Email_Management.Email_System_v2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import java.sql.ResultSet;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;

public class UI extends Application {

    private boolean composing = false;
    private Stage stage;
    private Scene scene;
    private Label subject;
    private Label sender;
    private Label content;
    private VBox viewer;

    String HoverColour = "-fx-background-color: rgba(99, 128, 241,0.25);";

    private void showLogin() {

        Label title = new Label("A-MAIL");
        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(\n" +
                        "    to right bottom,\n" +
                        "    #2e4285,\n" +
                        "    #683378\n" +
                        ");"
        );

        Label version = new Label("v2");

        version.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-style: italic;" +
                        "-fx-text-fill: linear-gradient(\n" +
                        "    to right bottom,\n" +
                        "    #2e4285,\n" +
                        "    #683378\n" +
                        ");"
        );

        VBox titleBox = new VBox(0);

        titleBox.setAlignment(Pos.CENTER);

        titleBox.getChildren().addAll(
                title,
                version
        );

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setMaxWidth(300);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(300);

        username.setStyle(
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                        "-fx-font-size: 16px;"
        );

        password.setStyle(
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                        "-fx-font-size: 16px;"
        );

        Button login = new Button("Login");
        login.setPrefWidth(142);
        login.setStyle(
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 20;"
        );

        login.setOnMouseEntered(e ->
                login.setStyle(
                        HoverColour +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-background-radius: 20;"
                )
        );

        login.setOnMouseExited(e ->
                login.setStyle(
                        "-fx-background-color: #2D2D2D;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-background-radius: 20;"
                )
        );

        VBox card = new VBox(20);

        Label status = new Label();

        status.setStyle(
                "-fx-font-size: 13px;"
        );


        HBox buttonBox = new HBox(15);


        login.setOnAction(event -> {

            User currentUser = Manage.getUser(
                    username.getText(),
                    password.getText()
            );

            if(currentUser != null){

                status.setText("");

                showDashboard(currentUser);
            }
            else{

                status.setStyle("-fx-text-fill: red;");
                status.setText("Invalid username or password");
                PauseTransition pause =
                        new PauseTransition(
                                Duration.seconds(3)
                        );

                pause.setOnFinished(
                        e -> status.setText("")
                );

                pause.play();
            }

        });


        Button register = new Button("Register");
        register.setPrefWidth(142);
        register.setStyle(
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 20;"
        );

        register.setOnMouseEntered(e ->
                register.setStyle(
                        HoverColour +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-background-radius: 20;"
                )
        );

        register.setOnMouseExited(e ->
                register.setStyle(
                        "-fx-background-color: #2D2D2D;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-background-radius: 20;"
                )
        );

        register.setOnAction(event -> {

            if(username.getText().isEmpty()
                    || password.getText().isEmpty()){
                return;
            }

            String result =
                    Manage.addUser(
                            username.getText(),
                            password.getText()
                    );

            status.setStyle("-fx-text-fill: #6366F1;");
            status.setText(result);
            PauseTransition pause =
                    new PauseTransition(
                            Duration.seconds(3)
                    );

            pause.setOnFinished(
                    e -> status.setText("")
            );

            pause.play();

        });


        buttonBox.getChildren().addAll(
                login,
                register
        );

        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                titleBox,
                username,
                password,
                status,
                buttonBox
        );

        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setSpacing(15);

        card.setStyle(
                "-fx-background-color: #1E1E1E;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 40;" +
                        "-fx-border-color: #2D2D2D;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 20;"
        );
        card.setEffect(new DropShadow());

        StackPane root = new StackPane();

        root.getChildren().add(card);

        root.setStyle(
                "-fx-background-color: linear-gradient(\n" +
                        "    to right bottom,\n" +
                        "    #182244,\n" +
                        "    #351a3d\n" +
                        ");"
        );


        if(scene == null){

            scene = new Scene(root);

            stage.setScene(scene);
        }
        else{

            scene.setRoot(root);
        }

        stage.setTitle("A-MAIL");
        this.stage = stage;

        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }

    private void showDashboard(User currentUser){

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom,#1A1630,#0e0e0e);"
        );

        // ===== SIDEBAR =====

        VBox sidebar = new VBox(15);
        sidebar.setAlignment(Pos.TOP_CENTER);

        Region sidebarSpacer = new Region();

        VBox.setVgrow(
                sidebarSpacer,
                Priority.ALWAYS
        );

        Label logo = new Label("Mail");

        logo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Button inbox = new Button("Inbox");
        Button sent = new Button("Sent");
        Button drafts = new Button("Drafts");
        Button spam = new Button("Spam");

        // Buttons not working
        sent.setOpacity(0.3);
        drafts.setOpacity(0.3);
        spam.setOpacity(0.3);

        sent.setDisable(true);
        drafts.setDisable(true);
        spam.setDisable(true);

        String navStyle =
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-pref-width: 180;" +
                        "-fx-pref-height: 40;";

        String hoverStyle =
                HoverColour +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-pref-width: 180;" +
                        "-fx-pref-height: 40;";

        Button[] buttons = {inbox, sent, drafts, spam};

        for(Button b : buttons) {

            b.setStyle(navStyle);

            b.setOnMouseEntered(
                    e -> b.setStyle(hoverStyle)
            );

            b.setOnMouseExited(
                    e -> b.setStyle(navStyle)
            );
        }
        Button DelAcc = new Button("Delete Account");
        Button logout = new Button("Logout");

        String logoutNormal =
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-radius: 20;" +
                        "-fx-pref-width: 180;" +
                                "-fx-pref-height: 40;";

        String logoutHover =
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: grey;" +
                        "-fx-border-radius: 20;" +
                        "-fx-pref-width: 180;" +
                        "-fx-pref-height: 40;";

        logout.setStyle(logoutNormal);
        DelAcc.setStyle(logoutNormal);

        DelAcc.setOnMouseEntered(
                e -> DelAcc.setStyle(logoutHover)
        );

        DelAcc.setOnMouseExited(
                e -> DelAcc.setStyle(logoutNormal)
        );

        logout.setOnMouseEntered(
                e -> logout.setStyle(logoutHover)
        );

        logout.setOnMouseExited(
                e -> logout.setStyle(logoutNormal)
        );

        logout.setOnAction(event -> {
            showLogin();
        });

        DelAcc.setOnAction(event -> {

            viewer.getChildren().clear();

            Label warning =
                    new Label("Delete Account");


            warning.setStyle(
                    "-fx-font-size: 28px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;"
            );

            Label msg =
                    new Label(
                            "This action cannot be undone."
                    );
            msg.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-font-weight: 0;" +
                            "-fx-text-fill: white;"
            );

            Button confirm =
                    new Button("Delete Forever");
            confirm.setStyle(
                    HoverColour +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 20;" +
                            "-fx-pref-width: 100;"
            );

            viewer.getChildren().addAll(
                    warning,
                    msg,
                    confirm
            );

        });

        sidebar.getChildren().addAll(
                logo,
                inbox,
                sent,
                drafts,
                spam,
                sidebarSpacer,
                logout,
                DelAcc
        );

        sidebar.setPadding(new Insets(25));

        sidebar.setStyle(
                "-fx-background-color: #1E1E1E;" +
                        "-fx-background-radius: 35;"
        );

        // ===== EMAIL LIST =====

        VBox emailList = new VBox(10);

        ScrollPane inboxScroll = new ScrollPane(
                emailList
        );

        inboxScroll.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;"
        );
        inboxScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        inboxScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        inboxScroll.setFitToWidth(true);

        inboxScroll.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;"
        );

        Label inboxTitle = new Label("Inbox");

        inboxTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;"
        );

        viewer = new VBox(20);

        subject = new Label(
                "Mail Viewer"
        );

        subject.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        sender = new Label(
                ""
        );


        sender.setStyle(
                "-fx-text-fill: #CCCCCC;" +
                        "-fx-font-size: 14px;"
        );


        content = new Label(
                "Select an email from the inbox.\n" +
                        "The email body will appear here."
        );

        content.setStyle(
                "-fx-text-fill: #CCCCCC;" +
                        "-fx-font-size: 16px;"
        );
        content.setWrapText(true);
        content.setPrefWidth(700);

        Button deleteMail = new Button("Delete");

        String deleteNormal =
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-radius: 20;" +
                        "-fx-pref-width: 100;" +
                        "-fx-pref-height: 20;";

        String deleteHover =
                "-fx-background-color: #2D2D2D;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: grey;" +
                        "-fx-border-radius: 20;" +
                        "-fx-pref-width: 100;" +
                        "-fx-pref-height: 20;";

        deleteMail.setStyle(deleteNormal);

        deleteMail.setOnMouseEntered(
                e -> deleteMail.setStyle(deleteHover)
        );

        deleteMail.setOnMouseExited(
                e -> deleteMail.setStyle(deleteNormal)
        );

        deleteMail.setVisible(false);

        try {

            ResultSet rs = Manage.getInbox(currentUser);
            final int[] selectedEmailId = {-1};

            while(rs.next()){

                String subjectText = rs.getString("subject");
                String bodyText = rs.getString("body");
                String senderText = rs.getString("sender");
                int emailId = rs.getInt("id");

                Button mail = new Button();
                mail.setText(
                        "From: " + senderText
                                + "\n"
                                + subjectText
                );
                String mailNormal =
                        "-fx-background-color: #1a1a1a;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 12;" +
                                "-fx-alignment: CENTER-LEFT;" +
                                "-fx-font-size: 14px;";

                String mailHover =
                        "-fx-background-color: #2D2D2D;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 12;" +
                                "-fx-alignment: CENTER-LEFT;" +
                                "-fx-font-size: 14px;";

                mail.setStyle(mailNormal);

                mail.setOnMouseEntered(
                        e -> mail.setStyle(mailHover)
                );

                mail.setOnMouseExited(
                        e -> mail.setStyle(mailNormal)
                );

                mail.setPrefWidth(450);
                mail.setPrefHeight(60);

                mail.setOnAction(e -> {

                    subject.setText(subjectText);
                    sender.setText(senderText);
                    content.setText(bodyText);

                    deleteMail.setVisible(true);
                    selectedEmailId[0] = emailId;

                });

                deleteMail.setOnAction(e -> {

                    Manage.deleteEmail(
                            selectedEmailId[0]
                    );

                    showDashboard(currentUser);
                });

                emailList.getChildren().add(mail);

                mail.setOnAction(e -> {
                    subject.setText(subjectText);
                    sender.setText(senderText);
                    content.setText(bodyText);

                    deleteMail.setVisible(true);
                    selectedEmailId[0] = emailId;
                });
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        VBox leftPanel = new VBox(20);

        leftPanel.getChildren().addAll(
                inboxTitle,
                inboxScroll
        );

        VBox.setVgrow(
                inboxScroll,
                Priority.ALWAYS
        );

        leftPanel.setPadding(new Insets(25));

        // ===== EMAIL VIEWER =====



        viewer.getChildren().addAll(
                subject,
                sender,
                content,
                deleteMail
        );

        viewer.setPadding(new Insets(25));

        viewer.setStyle(
                "-fx-background-color: linear-gradient(\n" +
                        "    to right bottom,\n" +
                        "    #182244,\n" +
                        "    #351a3d\n" +
                        ");" +
                        "-fx-background-radius: 20;"
        );

        HBox centerArea = new HBox(20);

        centerArea.getChildren().addAll(
                leftPanel,
                viewer
        );

        HBox.setHgrow(viewer, Priority.ALWAYS);

        // ===== TOP BAR =====

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        HBox topBar = new HBox(
                10,
                spacer
        );

        topBar.setPadding(new Insets(20));

        // ===== LAYOUT =====

        root.setLeft(sidebar);
        root.setTop(topBar);
        VBox mainContent = new VBox(20);

        Label title = new Label(
                "Welcome " + currentUser.Name
        );

        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Button compose = new Button("+  Compose");

        String composeStyleN = HoverColour +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-pref-width: 100;" +
                "-fx-pref-height: 40;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 20;";

        String composeNormal =
                composeStyleN +
                        "-fx-border-color: transparent;";

        String composeHover =
                composeStyleN +
                        "-fx-border-color: gray;";

        compose.setStyle(composeNormal);

        compose.setOnMouseEntered(
                e -> compose.setStyle(composeHover)
        );

        compose.setOnMouseExited(
                e -> compose.setStyle(composeNormal)
        );


        compose.setOnAction(event -> {

            if(composing){

                composing = false;

                viewer.getChildren().clear();

                subject.setText("Mail Viewer");
                sender.setText("");
                content.setText(
                        "Select an email from the inbox.\n" +
                                "The email body will appear here."
                );

                viewer.getChildren().addAll(
                        subject,
                        sender,
                        content
                );

                return;
            }

            composing = true;

            viewer.getChildren().clear();

            Label titleCompose =
                    new Label("Compose Mail");

            titleCompose.setStyle(
                    "-fx-font-size: 28px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;"
            );

            TextField receiver = new TextField();

            receiver.setPromptText("Send to...");

            receiver.setStyle(
                    "-fx-background-color: #1A1A1A;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 12;"
            );

            TextField subjectField = new TextField();

            subjectField.setPromptText("Subject");

            subjectField.setStyle(
                    "-fx-background-color: #1A1A1A;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 12;"
            );

            TextArea body = new TextArea();
            body.setWrapText(true);

            VBox.setVgrow(
                    body,
                    Priority.ALWAYS
            );

            body.setPromptText("Write your message...");

            body.setPrefHeight(200);

            body.setStyle(
                    "-fx-control-inner-background: #1A1A1A;" +
                            "-fx-text-fill: white;" +
                            "-fx-prompt-text-fill: #888888;" +
                            "-fx-highlight-fill: #6366F1;" +
                            "-fx-highlight-text-fill: white;" +
                            "-fx-background-color: #1A1A1A;" +
                            "-fx-background-insets: 0;" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: transparent;" +
                            "-fx-border-width: 0;" +
                            "-fx-font-size: 18;"
            );

            Button send = new Button("Send");

            send.setStyle(
                    HoverColour +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 20;" +
                            "-fx-pref-width: 100;"
            );

            HBox sendBar = new HBox();

            Region spacer1 = new Region();

            HBox.setHgrow(
                    spacer1,
                    Priority.ALWAYS
            );

            sendBar.getChildren().addAll(
                    spacer1,
                    send,
                    spacer
            );

            send.setOnAction(e -> {

                String receiverName =
                        receiver.getText();

                if(!Manage.userExists(
                        receiverName
                )){
                    return;
                }

                Manage.sendEmail(
                        currentUser.Name,
                        receiverName,
                        subjectField.getText(),
                        body.getText()
                );

                composing = false;

                showDashboard(currentUser);

                showDashboard(currentUser);

            });

            viewer.getChildren().addAll(
                    titleCompose,
                    receiver,
                    subjectField,
                    body,
                    sendBar
            );
        });

        HBox Bar = new HBox(
                50,
                title,
                compose
        );

        mainContent.getChildren().addAll(
                Bar,
                centerArea
        );

        mainContent.setPadding(new Insets(20));

        root.setCenter(mainContent);

        scene.setRoot(root);
    }

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        showLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}