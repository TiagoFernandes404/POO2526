o crontrollertotal tem alguns erros para ser um modelo mvc tem algumas coisas que tinha que tar no view pleo menos duas.
os menus tbm para navegar deviams er altereados de algma forma para ficarem mais legiveis.



 🔴 User sem subclasses (o maior problema)
O enunciado distingue claramente dois tipos de utilizador (admin vs. utilizador normal). O correto seria AdminUser extends User e RegularUser extends User, ou pelo menos uma superclasse abstrata com comportamento polimórfico. Usar um boolean admin é o anti-padrão clássico que o professor critica — e ainda há um botão no menu para qualquer utilizador se autopromover, o que é um bug de design sério.
🔴 getTopRoomsByDeviceCount usa parsing de strings para comparar números
O método formata objetos em strings ("Casa › Sala (3 dispositivos)") e depois faz replaceAll(".*\\((\\d+).*", "$1") para extrair o número e comparar. Isto é design muito fraco — o sort devia operar sobre os objetos Room diretamente.
🟠 Automações e escalonamentos globais, não associados à casa
O enunciado diz explicitamente que automações e escalonamentos são definidos por casa. No código estão em listas globais no DomusController, sem ligação a nenhuma House.
🟠 UI de automações só suporta TurnOnAction
O createAutomation() e o createSchedule() no ControllerTotal criam sempre uma TurnOnAction. Não há forma de criar automações com TurnOff, SetBrightness ou outras ações pela interface.
🟠 Condition só tem uma implementação (SensorCondition)
O enunciado menciona condições temporais além de condições externas. A interface Condition existe e está bem desenhada, mas falta uma TimeCondition ou similar. Seria fácil de fazer e daria pontos de design.
🟡 Blinds e GarageDoor são classes completamente vazias
Herdam tudo de ApertureDevice sem adicionar nada. O professor pode aceitar se a justificação for extensibilidade futura, mas é uma crítica fácil de fazer. Podia haver pelo menos um comportamento diferente entre portão e cortina.
🟡 Password em plaintext + sem validação de email
Menor em termos de OOP, mas mostra falta de robustez. Não afeta a nota estrutural mas pode ser apontado na apresentação.
🟡 SetBrightnessAction acopla-se a SmartLight em vez de Dimmable
O campo é SmartLight light quando devia ser Dimmable light. Viola o princípio de programar para a interface — a ação não precisa de saber que é uma SmartLight, só precisa de saber que é Dimmable. 
